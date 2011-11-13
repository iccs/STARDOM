<#import "/spring.ftl" as spring/>
<#macro loader id>
    <span id="${id}-loader" style="display:none">
        <img src="<@spring.url "/static/images/ajax-loader.gif"/>"/>
    </span>
</#macro>
