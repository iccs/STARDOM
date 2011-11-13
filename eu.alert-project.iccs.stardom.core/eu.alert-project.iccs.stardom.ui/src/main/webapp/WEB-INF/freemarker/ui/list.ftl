<#import "/spring.ftl" as spring/>
<div class="header clearfix">
    <div class="identity">
        Identity
    </div>
    <div class="profiles">
        Profile
    </div>
    <div class="metrics">
        Metrics
    </div>
    <div class="ci">
        CI
    </div>
</div>
<ul class="identity-list">
    <#list identities as identityBean>
        <li class="identity clearfix">
            <div class="identity">
                <img src="<@spring.url "/static/images/identity.png"/>" alt="Identity" border="none"/>
                <br/>
                <p class="identity-uuid">${identityBean.identity.uuid}</p>
            </div>
            <div class="profiles">
                <#list identityBean.identity.profiles as profile>
                    <#include "../identity/profile.ftl"/>
                </#list>
            </div>
            <div class="metrics">
                <#include "../identity/metrics.ftl"/>
            </div>
            <div class="ci">
                ${identityBean.ci}
            </div>
        </li>
    </#list>
</ul>
