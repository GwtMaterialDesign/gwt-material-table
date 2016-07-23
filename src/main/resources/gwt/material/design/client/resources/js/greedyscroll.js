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
;
(function ($, window, undefined) {
  "use strict";

  var name = "greedyScroll",
    id = 0,
    defaults = {
      topScrollOnFocus: true,
      topScrollMargin: 3
    };

  function Plugin(el, options) {
    // To avoid scope issues, use "base" instead of "this"
    // to reference this class from internal events and functions.
    var base = this;

    // Access to jQuery and DOM versions of element
    base.$el = $(el);
    base.el = el;
    base.id = id++;

    base.$window = $(window);
    base.$body = $("body");
    base.locked = [];

    // Listen for destroyed, call teardown
    base.$el.bind("destroyed", $.proxy(base.teardown, base));

    base.init = function () {
      base.options = $.extend({}, defaults, options);

      base.$el.attr("tabindex", 0);

      base.bind();
    };

    base.destroy = function () {
      base.$el.unbind("destroyed", base.teardown);
      base.teardown();
    };

    base.teardown = function () {
      $.removeData(base.el, "plugin_" + name);
      base.unbind();

      base.el = null;
      base.$el = null;
    };

    base.bind = function () {
      base.$el
        .on("focus." + name, base.lockParents)
        .on("blur." + name, base.unlockParents)
        .on("scroll." + name, $.throttle(100, base.scroll));
    };

    base.unbind = function () {
      base.$el.off("." + name);
    };

    base.scroll = function(e) {
      var $this = $(this);

      if(!$this.isScrollEnd() && !$this.isScrollStart()) {
        base.lockParents();
      } else if(!$this.is(":focus")) {
        base.unlockParents();
      }
    };

    base.lockParents = function() {
      // Ensure we are at the top of the panel
      if(base.options.topScrollOnFocus) {
        var top = base.$el.offset().top,
            scrollTop = base.$body.scrollTop();

        if(scrollTop > top) {
          base.$body.scrollTop(top - base.options.topScrollMargin);
        }
      }

      base.$body.addClass("overflow-hidden");

      $(this).parents().each(function(i) {
        var $this = $(this);

        if($this.hasScrollBar()) {
          base.locked[i] = $this;
          $this.addClass("overflow-hidden");
        }
      });
    };

    base.unlockParents = function() {
      base.$body.removeClass("overflow-hidden");

      for(var i in base.locked) {
        base.locked[i].removeClass("overflow-hidden");
      }
    };

    // Run initializer
    base.init();
  }

  // A plugin wrapper around the constructor,
  // preventing against multiple instantiations
  $.fn[name] = function (options) {
    return this.each(function () {
      var instance = $.data(this, "plugin_" + name);
      if (instance) {
        if (typeof options === "string") {
          instance[options].apply(instance);
        } else {
          instance.updateOptions(options);
        }
      } else if (options !== "destroy") {
        $.data(this, "plugin_" + name, new Plugin(this, options));
      }
    });
  };

})(jQuery, window);
