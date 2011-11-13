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
    }
}