<#import "/spring.ftl" as spring/>
<#macro loader id>
    <span id="${id}-loader" style="display:none">
        <img src="<@spring.url "/static/images/ajax-loader.gif"/>"/>
    </span>
</#macro>
<#macro field name>
    <div class="form-field">
    <div class="form-label">
        <@spring.message "${name}" />
    </div>
    <div class="form-input">
        <@spring.bind "${name}" />
        <input type="text" name="${spring.status.expression}" value="${spring.status.value?default("")}" />
    </div>
    <div class="form-errors">
        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
    </div>
    </div>

</#macro>

<#macro textField name>
    <div class="form-field">
    <div class="form-label">
        <@spring.message "${name}" />
    </div>
    <div class="form-input">
        <@spring.bind "${name}" />
        <textarea cols="40" rows="5" type="text" name="${spring.status.expression}" value="${spring.status.value?default("")}" ></textarea>
    </div>
    <div class="form-errors">
        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
    </div>
    </div>

</#macro>

