<div class="metrics-list">
    <ul>
        <#list identityBean.metrics as metric>
            <#assign trCss = (metric_index % 2 == 0)?string("","odd")>
            <li class="${trCss}">
                <label>${metric.label}</label>${metric.value}
            </li>
        </#list>
    </ul>

</div>
