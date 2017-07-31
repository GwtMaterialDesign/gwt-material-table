/*
 * #%L
 * GwtMaterialDesign
 * %%
 * Copyright (C) 2015 GwtMaterial
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * @author Ben Dol
 */
TableSubHeaders.id = 0;

TableSubHeaders.newInstance = function($table, $stickies) {
  return new TableSubHeaders($table, $stickies);
};

function TableSubHeaders($table, $stickies) {
  TableSubHeaders.id++;

  // To avoid scope issues, use "base" instead of "this"
  // to reference this class from internal events and functions.
  var base = this,
      $base = $(base);

  base.name = "subheaders" + TableSubHeaders.id;
  base.$window = $(window);
  base.$document = $(document);
  base.$tableBody = $table.find("tbody");
  base.$tableParents = $table.parents();
  base.$stickies = null;
  base.options = [];
  base.cache = [];

  base.loaded = false;
  base.scrollBarWidth = 13;
  base.debug = {
    enabled: false,
    smartScroll: { table: 0, window: 0, outer: 0 },
    touchmove: { table: 0, window: 0, outer: 0 },
    resize: { table: 0, window: 0 }
  };

  var defaults = {
    marginTop: 0,
    marginLeft: 0,
    scrollThrottle: 10,
    resizeThrottle: 100
  };

  base.load = function (opts) {
    base.options = $.extend({}, defaults, opts);

    // Calculate scrollbar width
    base.scrollBarWidth = $.scrollBarWidth();

    base.bind();
    base.detect();

    base.recalculate(true);
    base.loaded = true;
  };

  base.unload = function () {
    base.options = [];

    base.unbind();

    base.$stickies.each(function() {
      var $this = $(this);
      $this.removeClass("fixed").removeClass("passed").removeAttr("style");
      $this.unwrap();
      $table.scrollTop(0);
    });

    base.loaded = false;
  };

  base.reload = function () {
    base.unload();
    base.load();
  };

  base.detect = function () {
    base.$stickies = $($stickies, $table);

    // Bind toggle click
    base.$stickies.off("." + base.name);
    base.$stickies.on("tap."+base.name+" click."+base.name, function(e) {
      base.toggle($(this));
    });

    var filtered = $($stickies, $table).filter(function() {
      return $(this).parent().is(":not(div)");
    });

    filtered.each(function () {
      $(this).wrap("<div style='width:100%;z-index:2;cursor:pointer;background-color:#fff;'/>");
    });
  };

  base.bind = function () {
    base.unbind();

    var scrollThrottle = base.options.scrollThrottle,
        resizeThrottle = base.options.resizeThrottle;

    $table.smartScroll(base.name, $.throttle(scrollThrottle, function(e, scroll) {
      if(base.debug.enabled) base.debug.smartScroll.table++;
      base.scroll(e, scroll);
    }));
    $table.on("resize." + base.name, $.debounce(resizeThrottle, function() {
      if(base.debug.enabled) base.resize.table++;
      base.recalculate(true);
    }));

    base.$window.smartScroll(base.name, $.throttle(scrollThrottle, function(e, scroll) {
      if(base.debug.enabled) base.debug.smartScroll.window++;
      base.alignment(e, scroll);
    }));
    base.$window.on("resize." + base.name, $.debounce(resizeThrottle, function() {
      if(base.debug.enabled) base.debug.resize.window++;
      base.recalculate(true);
    }));

    base.detectOuterScrolls();
  };

  base.unbind = function () {
    $table.off("." + base.name);
    base.$window.off("." + base.name);

    // Unbind sticky events
    if(base.$stickies) {
      base.$stickies.off("." + base.name);
    }

    // Unbind 'scroll' on parent elements that have a scroll bar.
    base.$tableParents.each(function(index, el) {
      $(this).off("." + base.name);
    });
  };

  base.detectOuterScrolls = function() {
    var scrollThrottle = base.options.scrollThrottle;

    base.$tableParents.off("." + base.name);

    // Bind parent elements that have a scroll bar.
    base.$tableParents.filter(function() {
      return $(this).hasScrollBar();
    }).smartScroll(base.name, $.throttle(scrollThrottle, function(e, scroll) {
      if(base.debug.enabled) base.debug.smartScroll.outer++;
      base.alignment(e, scroll);
    }));
  };

  base.toggle = function($subheader) {
    if($subheader.hasClass("expanded")) {
      base.close($subheader);
    } else {
      base.open($subheader);
    }
  };

  base.open = function($subheader) {
    $subheader.addClass("expanded");

    // Trigger an 'opening' event
    $base.trigger("opening", [$subheader]);

    // Get all until next subheader

    var $icon = $subheader.find("i");
    if($icon != null) {
      var closeIcon = $subheader.attr("data-close-icon");
      if(closeIcon === undefined) {
        closeIcon = "remove";
      }
      $icon.text(closeIcon);
    }

    var parent = $subheader.parent();
    // Get rows by 'tr' rather than 'tr.data-row' since
    // expansion content should be touched also.
    var rows = parent.nextUntil("div", "tr");

    // Invoke .show and ensure recalculation after finished.
    // Need to recalculate the positions after showing
    // to ensure the sticky headers follow.
    $.when(rows.show(200)).then(function() {
      base.recalculate(true);

      // Trigger an 'opened' event
      $subheader.trigger("opened", [$subheader]);
    });
  };

  base.close = function($subheader) {
    $subheader.removeClass("expanded");

    // Trigger a 'closing' event
    $base.trigger("closing", [$subheader]);

    var $icon = $subheader.find("i");
    if($icon != null) {
      var openIcon = $subheader.attr("data-open-icon");
      if(openIcon === undefined) {
        openIcon = "add";
      }
      $icon.text(openIcon)
    }

    // Get all until next subheader
    var rows = $subheader.parent().nextUntil("div");

    // Invoke .hide and ensure recalculation after finished.
    // Need to recalculate the positions after showing
    // to ensure the sticky headers follow.
    $.when(rows.hide(200)).then(function() {
      base.recalculate(true);

      // Trigger a 'closed' event
      $subheader.trigger("closed", [$subheader]);
    });
  };

  base.recalculate = function(fireEvents) {
    if(fireEvents) {
      $base.trigger("before-recalculate");
    }

    // Detect any new outer scrolls
    base.detectOuterScrolls();

    // Calculate any new heights
    base.updateHeights();

    var scrollTop = $table.scrollTop(),
        outerScrollTop = base.getOuterScrollTop(),
        fullWidth = base.$tableBody.outerWidth();

    base.$stickies.each(function () {
      var $this = $(this);

      $this.removeClass("fixed").removeClass("passed").removeAttr("style");

      // Calculate the new position
      $.data(this, "pos", $this.offset().top + outerScrollTop + scrollTop + 1);

      base.updateWidth($this, fullWidth - 2);
    });

    // By default this method will fire, even when bound
    // to any particular jQuery event.
    if(fireEvents) {
      $base.trigger("after-recalculate");
    }

    base.scroll();
  };

  // Attempts to perform top scrolls using position: fixed since its the only
  // way to retain flexible styling with high performance (on mobile, etc).
  // This method has more overhead to ensure the subheader positions maintain 
  // within the boundries of the table (i.e. automatic edge clipping, etc), 
  // especially with multiple scrollable layers.
  base.scroll = function (e, scroll) {
    var offset = $table.offset(),
        clipWidth = $table.innerWidth(),
        fullWidth = base.$tableBody.outerWidth(),

        // Top variables
        top = offset.top + base.options.marginTop,
        scrollTop = $table.scrollTop() + base.getOuterScrollTop(),
        winScrollTop = base.$window.scrollTop(),

        // Left variables
        left = offset.left + base.options.marginLeft,
        scrollLeft = $table.scrollLeft(),
        outerScrollLeft = base.getOuterScrollLeft();

    base.$stickies.each(function (i) {
      var $this = $(this),
          forceXScroll = false;

      if(!scroll || scroll.isY()) {
        var $nextSticky = base.$stickies.eq(i + 1),
            $prevSticky = base.$stickies.eq(i - 1),
            pos = $.data(this, "pos");

        // Y Axis Scrolling
        if (pos <= scrollTop + top) {
          base.emulateYScroll($this, top, winScrollTop, scrollTop);

          if(!$this.hasClass("passed")) {
            if(!$this.hasClass("fixed")) {
              $this.addClass("fixed");

              // Ensure the X scroll is performed when
              // the Y scroll sticks a new subheader.
              forceXScroll = scroll && !scroll.isX();
            }

            // Update the subheaders width
            base.updateWidth($this, fullWidth);
            
            $prevSticky.css("display", "none");

            // Try display the current sticky
            $this.css({ "display": "" });

            if ($nextSticky.length > 0 && scrollTop + top >= pos - $prevSticky.outerHeight()) {
              $this.addClass("passed");
            }
          }
        } else {
          var width = $this.outerWidth();
          $this.removeClass("fixed").removeAttr("style");
          //$this.width(width);

          // Try display the previous sticky
          $prevSticky.css({ "display": "" });

          if ($prevSticky.length > 0 && scrollTop + top <= pos - $prevSticky.outerHeight()) {
            $prevSticky.removeClass("passed");
          }
        }
      }

      // X Axis Scrolling
      if(!scroll || scroll.isX() || forceXScroll) {
        if($this.hasClass("fixed")) {
          base.emulateXScroll($this, clipWidth, left, scrollLeft, outerScrollLeft);
        }
      }
    });

    // Update passed subheaders top position.
    base.$stickies.filter(".passed").each(function(i, el) {
      var $this = $(this),
          $nextSticky = base.$stickies.eq(i + 1);

      var newTop = $.data($nextSticky[0], "pos") - scrollTop - $this.outerHeight() - winScrollTop;
      $this.data("push-back", parseInt($this.css("top").replace("px", "") - newTop));
      $this.css({ "top": newTop });

      // TODO: Fix the clip issue
      // base.emulateYScroll($this, newTop, winScrollTop, scrollTop);
    });
  };

  base.emulateYScroll = function($sticky, top, winScrollTop, scrollTop, outerScroll) {
    var height = $sticky.outerHeight() + 50,
        totalTop = (outerScroll ? outerScroll : base.getOuterScrollTop()) - base.options.marginTop;

    var topClip = totalTop + "px",
        bottomClip = height + "px",
        rightClip = $sticky.data("clip-right"),
        leftClip = $sticky.data("clip-left");

    // Handle the passed clipping
    if($sticky.hasClass("passed")) {
      var pushBack = $sticky.data("push-back");
       topClip = (totalTop + pushBack) + "px";
    }

    // Update Y clipping data
    $sticky.data("clip-top", topClip)
           .data("clip-bottom", bottomClip);

    $sticky.css({
      "top": top - winScrollTop,
      "clip": "rect("+ topClip +", " + rightClip + ", " + bottomClip + ", " + leftClip + ")"
    });
  };

  base.emulateXScroll = function($sticky, tableWidth, left, scrollLeft, outerScroll) {
    var leftClip = (scrollLeft + (outerScroll ? outerScroll : base.getOuterScrollLeft())) + "px",
        rightClip = (tableWidth + scrollLeft - base.scrollBarWidth) + "px",
        topClip = $sticky.data("clip-top") || "auto",
        bottomClip = $sticky.data("clip-bottom") || "auto";

    // Update X clipping data
    $sticky.data("clip-left", leftClip)
           .data("clip-right", rightClip);

    $sticky.css({
      "left": left - scrollLeft - base.$window.scrollLeft(),
      "clip": "rect("+ topClip +", " + rightClip + ", " + bottomClip + ", " + leftClip + ")"
    });
  };

  // Applies the alignment of all the outer scroll positions.
  base.alignment = function(e, scroll) {
    var offset = $table.offset(),
        clipWidth = $table.innerWidth(),
        fullWidth = base.$tableBody.outerWidth(),
        xScroll = !scroll || scroll.isX(),
        yScroll = !scroll || scroll.isY(),

        // Top variables
        winScrollTop = (yScroll ? base.$window.scrollTop() : 0),
        top = (yScroll ? offset.top + base.options.marginTop : 0),
        scrollTop = (yScroll ? $table.scrollTop() + base.getOuterScrollTop() : 0),

        // Left variables
        left = (xScroll ? offset.left + base.options.marginLeft : 0),
        scrollLeft = (xScroll ? $table.scrollLeft() : 0),
        outerScrollLeft = (xScroll ? base.getOuterScrollLeft() : 0);

    base.$stickies.filter(".fixed").each(function (i) {
      var $this = $(this);

      if(!scroll || scroll.isY()) {
        var newTop = top;

        // Calculate 'passed' stickies position based on outer scrollTop's.
        if($this.hasClass("passed")) {
          var $nextSticky = base.$stickies.eq(i + 1);
          newTop = $.data($nextSticky[0], 'pos') - scrollTop - $this.outerHeight();
        }

        base.emulateYScroll($this, newTop, winScrollTop, scrollTop);

        // When the scrollbar hides or displays
        base.updateWidth($this, fullWidth);
      }

      if(xScroll) {
        base.emulateXScroll($this, clipWidth, left, scrollLeft, outerScrollLeft);
      }
    });
  };

  base.getOuterScrollTop = function() {
    var scrollTop = 0;

    base.$tableParents.filter(":not(body,html)").each(function() {
      scrollTop += $(this).scrollTop();
    });
    return scrollTop;
  };

  base.getOuterScrollLeft = function() {
    var scrollLeft = 0;

    base.$tableParents.filter(":not(body,html)").each(function() {
      scrollLeft += $(this).scrollLeft();
    });
    return scrollLeft;
  };

  base.updateCellWidths = function ($sticky) {
    // Copy cell widths from header
    var cellWidths = base.getCellWidths($("th,td", $("thead", $table)));
    base.setCellWidths(cellWidths, $sticky);
  };

  base.setCellWidths = function (widths, $sticky) {
    $sticky.find("th,td").each(function (index) {
      var $this = $(this),
          width = widths[index];

      $this.css({
        "min-width": width,
        "max-width": width
      });
    });
  };

  base.getCellWidths = function ($headers) {
    var widths = [];
    $headers.each(function (index) {
      var width, $this = $(this);

      if ($this.css("box-sizing") === "border-box") {
        // when border-box types are used
        width = this.getBoundingClientRect().width;
      } else {
        var $origTh = $("th", base.$originalHeader);
        if ($origTh.css("border-collapse") === "collapse") {
          if (window.getComputedStyle) {
            width = parseFloat(window.getComputedStyle(this, null).width);
          } else {
            // ie8 only
            var leftPadding = parseFloat($this.css("padding-left"));
            var rightPadding = parseFloat($this.css("padding-right"));
            // Needs more investigation - this is assuming constant border 
            // around this cell and it's neighbours.
            var border = parseFloat($this.css("border-width"));
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

  base.updateWidth = function($sticky, width) {
    /*if(width) {
      $sticky.width(width);
    }*/

    // Calculate new cell widths
    base.updateCellWidths($sticky);
  };

  base.updateWidths = function() {
    /*base.$stickies.each(function() {
      base.updateWidth($(this));
    });*/
  };

  base.updateHeights = function() {
    base.$stickies.each(function() {
      var $this = $(this);
      $this.parent().height($this.outerHeight());
    });
  };

  base.setMarginTop = function(marginTop) {
    base.options.marginTop = marginTop;
  };

  base.setMarginLeft = function(marginLeft) {
    base.options.marginLeft = marginLeft;
  };

  base.isLoaded = function() {
    return base.loaded;
  };

  base.getDebugInfo = function() {
    return base.debug;
  }
}
