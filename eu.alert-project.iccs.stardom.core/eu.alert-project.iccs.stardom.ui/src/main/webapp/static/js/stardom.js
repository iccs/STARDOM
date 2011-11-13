var ICCS = {
    isLocked: function(button) {
        var b = jQuery(button);

        if (b.attr("disabled")) {
            return true;
        }
        return false;
    },
    lockLink:function(button, disabledText) {

        var b = jQuery(button);
        b.html(disabledText);
        b.attr("disabled", 'true');
    },

    unlockLink:function(button, text) {
        var b = jQuery(button);
        b.html(text);
        b.removeAttr("disabled");
    },
    lockButton:function(button, disabledText) {

        var b = jQuery(button);
        b.attr("value",disabledText);
        b.attr("disabled", 'true');
    },

    unlockButton:function(button, text) {
        var b = jQuery(button);
        b.attr("value",text);
        b.removeAttr("disabled");
    },

    refreshPages:function(id){

        $.getJSON("/ui/pagination.json",function(data){

            var page = data.selected;
            var pages= data.pages;

            var result = "<ul class=\"pages\">";

            for(var i=0; i< pages.length; i++){

                var p =pages[i];
                result+="<li class=\"page "+(page==p ? 'selected':'')+"\">"+p+"</li>";
            }
            result +="</ul>";

            $(id).html(result);
        });
    },
    setView:function(source,url,viewId,pagesId,loaderId){

        $(loaderId).fadeIn();
        $.get(url,function(data){

            ICCS.unlockLink(source);

            $(viewId).html(data);

            ICCS.refreshPages(pagesId);
            $(loaderId).fadeOut();
        });
    }

}