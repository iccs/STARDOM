<div class="form-field">
    <div class="form-label">
        <@spring.message "identity.name"/>
    </div>
    <div class="form-input">
        <@spring.bind "identity.name" />
        <input type="text" name="${spring.status.expression}" value="${spring.status.value?default("")}" />
    </div>
    <div class="form-errors">
        <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
    </div>
</div>
