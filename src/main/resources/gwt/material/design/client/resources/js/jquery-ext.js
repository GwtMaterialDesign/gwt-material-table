/*
 * jQuery throttle / debounce - v1.1 - 3/7/2010
 * http://benalman.com/projects/jquery-throttle-debounce-plugin/
 * 
 * Copyright (c) 2010 "Cowboy" Ben Alman
 * Dual licensed under the MIT and GPL licenses.
 * http://benalman.com/about/license/
 */
(function(b,c){var $=b.jQuery||b.Cowboy||(b.Cowboy={}),a;$.throttle=a=function(e,f,j,i){var h,d=0;if(typeof f!=="boolean"){i=j;j=f;f=c}function g(){var o=this,m=+new Date()-d,n=arguments;function l(){d=+new Date();j.apply(o,n)}function k(){h=c}if(i&&!h){l()}h&&clearTimeout(h);if(i===c&&m>e){l()}else{if(f!==true){h=setTimeout(i?k:l,i===c?e-m:e)}}}if($.guid){g.guid=j.guid=j.guid||$.guid++}return g};$.debounce=function(d,e,f){return f===c?a(d,e,false):a(d,f,e!==false)}})(this);

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

/*
 * jQuery Extension Library v1.0
 *
 * @author Ben Dol
 */
(function($) {
  $.fn.insertAt = function(index, element) {
    var lastIndex = this.children().size();
    if (index < 0) {
      index = Math.max(0, lastIndex + 1 + index)
    }
    this.append(element);
    if (index < lastIndex) {
      this.children().eq(index).before(this.children().last())
    }
    return this;
  };

  $.fn.longpress = function(longCallback, shortCallback, duration) {
    if (typeof duration === "undefined") {
      duration = 500;
    }

    return this.each(function() {
      var $this = $(this);

      // to keep track of how long something was pressed
      var mouse_down_time;
      var timeout;

      // mousedown or touchstart callback
      function mousedown_callback(e) {
        mouse_down_time = new Date().getTime();
        var context = $(this);

        // set a timeout to call the longpress callback when time elapses
        timeout = setTimeout(function() {
          if (typeof longCallback === "function") {
            longCallback.call(context, e);
          } else {
            $.error('Callback required for long press. You provided: ' + typeof longCallback);
          }
        }, duration);
      }

      // mouseup or touchend callback
      function mouseup_callback(e) {
        var press_time = new Date().getTime() - mouse_down_time;
        if (press_time < duration) {
          // cancel the timeout
          clearTimeout(timeout);

          // call the shortCallback if provided
          if (typeof shortCallback === "function") {
            shortCallback.call($(this), e);
          } else if (typeof shortCallback === "undefined") {
            // nothing for now
          } else {
            $.error('Optional callback for short press should be a function.');
          }
        }
      }

      // cancel long press event if the finger or mouse was moved
      function move_callback(e) {
        clearTimeout(timeout);
      }

      // Browser Support
      $this.on('mousedown', mousedown_callback);
      $this.on('mouseup', mouseup_callback);
      $this.on('mousemove', move_callback);

      // Mobile Support
      $this.on('touchstart', mousedown_callback);
      $this.on('touchend', mouseup_callback);
      $this.on('touchmove', move_callback);
      return this;
    });
  };

  $.fn.hasVerticalScrollBar = function() {
    return this.get(0) ? this.get(0).scrollHeight > this.innerHeight() : false;
  };

  $.fn.hasHorizontalScrollBar = function() {
    return this.get(0) ? this.get(0).scrollWidth > this.innerWidth() : false;
  };

  $.fn.hasScrollBar = function() {
    return this.hasVerticalScrollBar() || this.hasHorizontalScrollBar();
  };

  $.scrollBarWidth = function() {
    var inner = document.createElement("p");
    inner.style.width = "100%";
    inner.style.height = "200px";
  
    var outer = document.createElement("div");
    outer.style.position = "absolute";
    outer.style.top = "0px";
    outer.style.left = "0px";
    outer.style.visibility = "hidden";
    outer.style.width = "200px";
    outer.style.height = "150px";
    outer.style.overflow = "hidden";
    outer.appendChild(inner);
  
    document.body.appendChild(outer);
    var w1 = inner.offsetWidth;
    outer.style.overflow = "scroll";
    var w2 = inner.offsetWidth;
    if (w1 === w2) w2 = outer.clientWidth;
  
    document.body.removeChild(outer);
  
    return (w1 - w2);
  };

  $.fn.isScrollEnd = function() {
    return this.scrollTop() >= this[0].scrollHeight - this.outerHeight();
  };

  $.fn.isScrollStart = function() {
    return this.scrollTop() <= this[0].scrollHeight - this.outerHeight();
  };

  $.fn.scrollHandler = function(dir, name, handler) {
    var base = this;
    var lastScroll = { x: 0, y: 0 };
    return base.on("scroll." + name, function(e) {
      var $this = $(this),
          scrollTop = $this.scrollTop(),
          scrollLeft = $this.scrollLeft();

      var scroll = {
            dirs: [], 
            isUp: function() { return $.inArray("up", this.dirs) > -1; },
            isDown: function() { return $.inArray("down", this.dirs) > -1; },
            isLeft: function() { return $.inArray("left", this.dirs) > -1; },
            isRight: function() { return $.inArray("right", this.dirs) > -1; },
            isX: function() { return this.isLeft() || this.isRight(); },
            isY: function() { return this.isUp() || this.isDown(); }
          }, 
          index = 0;
      if(lastScroll.y > scrollTop) {
        scroll.dirs[index++] = "up";
      } else if(lastScroll.y !== scrollTop) {
        scroll.dirs[index++] = "down";
      }

      if(lastScroll.x > scrollLeft) {
        scroll.dirs[index++] = "left";
      } else if(lastScroll.x !== scrollLeft) {
        scroll.dirs[index++] = "right";
      }

      if(dir) {
        if($.isArray(dir)) {
          for(var d in scroll.dirs) {
            if($.inArray(scroll.dirs[d], dir) > -1) {
              handler(e, scroll);
              break; // Found at least one match
            }
          }
        } else if(dir === "any" || $.inArray(dir, scroll.dirs) > -1) {
          handler(e, scroll);
        }
      } else {
        handler(e, scroll);
      }

      lastScroll = { x: scrollLeft,  y: scrollTop };
    });
  };

  $.fn.smartScroll = function(name, handler) {
    return this.scrollHandler("any", name, handler);
  };

  $.fn.scrollY = function(name, handler) {
    return this.scrollHandler(["up", "down"], name, handler);
  };

  $.fn.scrollX = function(name, handler) {
    return this.scrollHandler(["left", "right"], name, handler);
  };

  $.fn.onScrollUp = function(name, handler) {
    return this.scrollHandler("up", name, handler);
  };

  $.fn.onScrollDown = function(name, handler) {
    return this.scrollHandler("down", name, handler);
  };

  $.fn.onScrollLeft = function(name, handler) {
    return this.scrollHandler("left", name, handler);
  };

  $.fn.onScrollRight = function(name, handler) {
    return this.scrollHandler("right", name, handler);
  };
})(jQuery);