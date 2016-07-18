/*! Copyright (c) 2011 by Jonas Mosbech - https://github.com/jmosbech/StickyTableHeaders
  MIT license info: https://github.com/jmosbech/StickyTableHeaders/blob/master/license.txt */

;
(function ($, window, undefined) {
  'use strict';

  var name = 'stickyTableHeaders',
    id = 0,
    defaults = {
      fixedOffset: 0,
      leftOffset: 0,
      marginTop: 0,
      scrollableArea: window
    };

  function Plugin(el, options) {
    // To avoid scope issues, use 'base' instead of 'this'
    // to reference this class from internal events and functions.
    var base = this;

    // Access to jQuery and DOM versions of element
    base.$el = $(el);
    base.el = el;
    base.id = id++;
    base.$window = $(window);
    base.$document = $(document);

    // Listen for destroyed, call teardown
    base.$el.bind('destroyed', $.proxy(base.teardown, base));

    // Cache DOM refs for performance reasons
    base.$clonedHeader = null;
    base.$originalHeader = null;

    // Keep track of state
    base.isSticky = false;
    base.hasBeenSticky = false;
    base.leftOffset = null;
    base.topOffset = null;
    base.scrollBarWidth = 13;

    base.init = function () {
      base.$el.each(function () {
        var $this = $(this);

        base.scrollBarWidth = $.scrollBarWidth();

        // remove padding on <table> to fix issue #7
        $this.css('padding', 0);

        base.$originalHeader = $('thead:first', this);
        base.$clonedHeader = base.$originalHeader.clone();
        $this.trigger('clonedHeader.' + name, [base.$clonedHeader]);

        base.$clonedHeader.addClass('tableFloatingHeader');
        base.$clonedHeader.css('display', 'none');

        base.$originalHeader.addClass('tableFloatingHeaderOriginal');
        base.$originalHeader.after(base.$clonedHeader);
        base.$clonedHeader.find(".stickyExclude").remove();

        base.$printStyle = $('<style type="text/css" media="print">' +
          '.tableFloatingHeader{display:none !important;}' +
          '.tableFloatingHeaderOriginal{position:static !important;}' +
          '</style>');
        $('head').append(base.$printStyle);
      });

      base.setOptions(options);
      base.updateWidth();
      base.toggleHeaders();
      base.bind();
    };

    base.destroy = function () {
      base.$el.unbind('destroyed', base.teardown);
      base.teardown();
    };

    base.teardown = function () {
      if (base.isSticky) {
        base.$originalHeader.css('position', 'static');
      }
      $.removeData(base.el, 'plugin_' + name);
      base.unbind();

      base.$clonedHeader.remove();
      base.$originalHeader.removeClass('tableFloatingHeaderOriginal');
      base.$originalHeader.css('visibility', 'visible');
      base.$printStyle.remove();

      base.el = null;
      base.$el = null;
    };

    base.bind = function () {
      base.$scrollableArea.on('scroll.' + name, base.toggleHeaders);
      if (!base.isWindowScrolling) {
        base.$window.on('scroll.' + name + base.id, base.setPositionValues);
        //base.$window.on("touchmove." + name + base.id, base.setPositionValues);
        base.$window.on('resize.' + name + base.id, base.toggleHeaders);
      }
      base.$scrollableArea.on('resize.' + name, base.toggleHeaders);
      base.$scrollableArea.on('resize.' + name, base.updateWidth);
      base.$window.on('resize.' + name, base.updateWidth);

      base.detectOuterScrolls();
    };

    base.unbind = function () {
      // unbind window events by specifying handle so we don't remove too much
      base.$scrollableArea.off('.' + name, base.toggleHeaders);
      if (!base.isWindowScrolling) {
        base.$window.off('.' + name + base.id, base.setPositionValues);
        base.$window.off('.' + name + base.id, base.toggleHeaders);
      }
      base.$scrollableArea.off('.' + name, base.updateWidth);
      base.$window.off('.' + name + base.id, base.updateWidth);

      // Unbind 'scroll' on parent elements that have a scroll bar.
      base.$scrollableArea.parents().each(function(index, el) {
        $(this).off("." + name);
      });
    };

    base.detectOuterScrolls = function() {
      // Bind parent elements that have a scroll bar.
      base.$scrollableArea.parents().each(function(index, el) {
        var $this = $(this);
        $this.off("." + name);
        if($this.hasScrollBar()) {
          // Ensure the subheaders keep thier alignment 
          // when outer scrolling occurs.
          $this.on("scroll." + name, base.toggleHeaders);
          //$this.on("touchmove." + name, base.toggleHeaders);
        }
      });
    };

    base.toggleHeaders = function () {
      if (base.$el) {
        base.$el.each(function() {
          var $this = $(this),
            newLeft,
            newTopOffset = base.isWindowScrolling ? (
            isNaN(base.options.fixedOffset) ? base.options.fixedOffset.outerHeight() 
              : base.options.fixedOffset) 
              : base.$scrollableArea.offset().top + (!isNaN(base.options.fixedOffset) ? base.options.fixedOffset : 0),

            offset = $this.offset(),

            scrollTop = base.$scrollableArea.scrollTop() + newTopOffset,
            scrollLeft = base.$scrollableArea.scrollLeft(),

            scrolledPastTop = base.isWindowScrolling ? scrollTop > offset.top : newTopOffset > offset.top,
            notScrolledPastBottom = (base.isWindowScrolling ? scrollTop : 0) < (offset.top + $this.height() 
              - base.$clonedHeader.height() - (base.isWindowScrolling ? 0 : newTopOffset));

          if (scrolledPastTop /*&& notScrolledPastBottom*/) {
            newLeft = offset.left /*- scrollLeft*/ + base.options.leftOffset;
            base.$originalHeader.css({
              'position': 'fixed',
              'margin-top': base.options.marginTop,
              'left': newLeft,
              'z-index': 3 // #18: opacity bug
            });
            base.leftOffset = newLeft;
            base.topOffset = newTopOffset;
            base.$clonedHeader.css('display', '');
            if (!base.isSticky) {
              base.isSticky = true;
              // make sure the width is correct: the user might have resized the browser while in static mode
              base.updateWidth();

              base.$el.trigger('sticky-change', [true]);
            }
            base.setPositionValues();
          } else if (base.isSticky) {
            base.$originalHeader.css('position', 'static');
            base.$clonedHeader.css('display', 'none');
            base.isSticky = false;
            base.resetWidths($('td,th', base.$clonedHeader), $('td,th', base.$originalHeader));

            base.$el.trigger('sticky-change', [false]);
          }
        });
      }
    };

    base.setPositionValues = function () {
      var winScrollTop = base.$window.scrollTop(),
        winScrollLeft = base.$window.scrollLeft();
      if (!base.isSticky /*|| winScrollTop < 0 || winScrollTop + base.$window.height() > base.$document.height()*/
          || winScrollLeft < 0 || winScrollLeft + base.$window.width() > base.$document.width()) {
        return;
      }

      base.detectOuterScrolls();

      var scrollLeft = base.$scrollableArea.scrollLeft(),
          leftClip = base.$scrollableArea.scrollLeft() + base.getOuterScrollLeft(),
          topClip = base.getOuterScrollTop() - base.options.marginTop,
          height = base.$originalHeader.outerHeight();

      base.$originalHeader.css({
        'top': base.topOffset - (base.isWindowScrolling ? 0 : winScrollTop),
        'left': base.leftOffset - (base.isWindowScrolling ? 0 : winScrollLeft),
        "clip": "rect(0px, " + (base.$scrollableArea.outerWidth() + scrollLeft - base.scrollBarWidth) + "px, " + (height + 50) + "px, " + leftClip + "px)"
      });
    };

    base.updateWidth = function () {
      if (!base.isSticky) {
        return;
      }
      // Copy cell widths from clone
      if (!base.$originalHeaderCells) {
        base.$originalHeaderCells = $('tr:not(".stickyExclude") th,td', base.$originalHeader);
      }
      if (!base.$clonedHeaderCells) {
        base.$clonedHeaderCells = $('tr:not(".stickyExclude") th,td', base.$clonedHeader);
      }
      var cellWidths = base.getWidths(base.$clonedHeaderCells);
      base.setWidths(cellWidths, base.$clonedHeaderCells, base.$originalHeaderCells);

      // Copy row width from whole table
      base.$originalHeader.css('width', base.$clonedHeader.width());
    };

    base.getWidths = function ($clonedHeaders) {
      var widths = [];
      $clonedHeaders.each(function (index) {
        var width, $this = $(this);

        if ($this.css('box-sizing') === 'border-box') {
          width = $this[0].getBoundingClientRect().width; // #39: border-box bug
        } else {
          var $origTh = $('th', base.$originalHeader);
          if ($origTh.css('border-collapse') === 'collapse') {
            if (window.getComputedStyle) {
              width = parseFloat(window.getComputedStyle(this, null).width);
            } else {
              // ie8 only
              var leftPadding = parseFloat($this.css('padding-left'));
              var rightPadding = parseFloat($this.css('padding-right'));
              // Needs more investigation - this is assuming constant border around this cell and it's neighbours.
              var border = parseFloat($this.css('border-width'));
              width = $this.outerWidth() - leftPadding - rightPadding - border;
            }
          } else {
            width = $this.width();
          }
        }

        widths[index] = width;
      });
      return widths;
    };

    base.setWidths = function (widths, $clonedHeaders, $origHeaders) {
      $clonedHeaders.each(function (index) {
        var width = widths[index];
        $origHeaders.eq(index).css({
          'min-width': width,
          'max-width': width
        });
      });
    };

    base.resetWidths = function ($clonedHeaders, $origHeaders) {
      $clonedHeaders.each(function (index) {
        var $this = $(this);
        $origHeaders.eq(index).css({
          'min-width': $this.css('min-width'),
          'max-width': $this.css('max-width')
        });
      });
    };

    base.getOuterScrollTop = function() {
      var scrollTop = 0;

      base.$scrollableArea.parents(":not(body)").each(function() {
        scrollTop += $(this).scrollTop();
      });
      return scrollTop;
    };

    base.getOuterScrollLeft = function() {
      var scrollLeft = 0;

      base.$scrollableArea.parents(":not(body)").each(function() {
        scrollLeft += $(this).scrollLeft();
      });
      return scrollLeft;
    };

    base.setOptions = function (options) {
      base.options = $.extend({}, defaults, options);
      base.$scrollableArea = $(base.options.scrollableArea);
      base.isWindowScrolling = base.$scrollableArea[0] === window;
    };

    base.updateOptions = function (options) {
      base.setOptions(options);
      // scrollableArea might have changed
      base.unbind();
      base.bind();
      base.updateWidth();
      base.toggleHeaders();
    };

    // Run initializer
    base.init();
  }

  // A plugin wrapper around the constructor,
  // preventing against multiple instantiations
  $.fn[name] = function (options) {
    return this.each(function () {
      var instance = $.data(this, 'plugin_' + name);
      if (instance) {
        if (typeof options === 'string') {
          instance[options].apply(instance);
        } else {
          instance.updateOptions(options);
        }
      } else if (options !== 'destroy') {
        $.data(this, 'plugin_' + name, new Plugin(this, options));
      }
    });
  };

})(jQuery, window);