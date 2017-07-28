mutate_event_stack = [
	{
		name: 'width',
		handler: function (elem){
			n = {el:elem}
			if(!$(n.el).data('mutate-width'))$(n.el).data('mutate-width', $(n.el).width());
			if ($(n.el).data('mutate-width')&&$(n.el).width() != $(n.el).data('mutate-width')  ) {
				$(n.el).data('mutate-width', $(n.el).width());
				return true;
			}
			return false;
		}
	},
	{
		name:'height',
		handler: function (n){
			element = n;
			if(!$(element).data('mutate-height'))$(element).data('mutate-height', $(element).height());
			if ($(element).data('mutate-height')&&$(element).height() != $(element).data('mutate-height')  ) {
				$(element).data('mutate-height', $(element).height());
				return true;
			}
		}
	},
	{
		name		: 'top',
		handler 	: function (n){
			if(!$(n).data('mutate-top'))$(n).data('mutate-top', $(n).css('top'));

			if ($(n).data('mutate-top')&&$(n).css('top') != $(n).data('mutate-top')  ) {
				$(n).data('mutate-top', $(n).css('top'));
				return true;
			}
		}
	},
	{
		name		: 'bottom',
		handler 	: function (n){
			if(!$(n).data('mutate-bottom'))$(n).data('mutate-bottom', $(n).css('bottom'));

			if ($(n).data('mutate-bottom')&&$(n).css('bottom') != $(n).data('mutate-bottom')  ) {
				$(n).data('mutate-bottom', $(n).css('bottom'));
				return true;
			}
		}
	},
	{
		name		: 'right',
		handler 	: function (n){
			if(!$(n).data('mutate-right'))$(n).data('mutate-right', $(n).css('right'));

			if ($(n).data('mutate-right')&&$(n).css('right') != $(n).data('mutate-right')  ) {
				$(n).data('mutate-right', $(n).css('right'));
				return true;
			}
		}
	},
	{
		name		: 'left',
		handler 	: function (n){
			if(!$(n).data('mutate-left'))$(n).data('mutate-left', $(n).css('left'));

			if ($(n).data('mutate-left')&&$(n).css('left') != $(n).data('mutate-left')  ) {
				$(n).data('mutate-left', $(n).css('left'));
				return true;
			}
		}
	},
	{
		name		: 'hide',
		handler 	: function (n){ if ($(n).is(':hidden'))	return true; }
	},
	{
		name		: 'show',
		handler 	: function (n){ if ($(n).is(':visible'))	return true; }
	},
	{
		name		: 'scrollHeight',
		handler 	: function (n){
			if(!$(n).data('prev-scrollHeight'))$(n).data('prev-scrollHeight', $(n)[0].scrollHeight);

			if ($(n).data('prev-scrollHeight')&&$(n)[0].scrollHeight != $(n).data('prev-scrollHeight')  ) {
				$(n).data('prev-scrollHeight', $(n)[0].scrollHeight);
				return true;
			}
		}
	},
	{
		name		: 'scrollWidth',
		handler 	: function (n){
			if(!$(n).data('prev-scrollWidth'))$(n).data('prev-scrollWidth', $(n)[0].scrollWidth);

			if ($(n).data('prev-scrollWidth')&&$(n)[0].scrollWidth != $(n).data('prev-scrollWidth')  ) {
				$(n).data('prev-scrollWidth', $(n)[0].scrollWidth);
				return true;
			}
		}
	},
	{
		name		: 'scrollTop',
		handler 	: function (n){
			if(!$(n).data('prev-scrollTop'))$(n).data('prev-scrollTop', $(n)[0].scrollTop());

			if ($(n).data('prev-scrollTop')&&$(n)[0].scrollTop() != $(n).data('prev-scrollTop')  ) {
				$(n).data('prev-scrollTop', $(n)[0].scrollTop());
				return true;
			}
		}
	},
	{
		name		: 'scrollLeft',
		handler 	: function (n){
			if(!$(n).data('prev-scrollLeft'))$(n).data('prev-scrollLeft', $(n)[0].scrollLeft());

			if ($(n).data('prev-scrollLeft')&&$(n)[0].scrollLeft() != $(n).data('prev-scrollLeft')  ) {
				$(n).data('prev-scrollLeft', $(n)[0].scrollLeft());
				return true;
			}
		}
	}
];