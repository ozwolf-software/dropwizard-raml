[#ftl output_format="HTML" auto_esc=false]

[#import "utility-tools.ftl" as utils/]

[#macro printParameterBadge label value = ""]
    <span class="badge badge-light"><span class="font-weight-bold">${label}</span>[#if value?has_content]: <span class="text-monospace">${value}</span>[/#if]</span>
[/#macro]

[#macro printCodeDialog modalId scope type content]
    <a href="#" data-toggle="modal" data-target="#${modalId}">${type}</a>
    <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">${scope} ${type}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body p-3">
                    <pre class="border border-secondary p-1">[#autoesc]${content}[/#autoesc]</pre>
                </div>
            </div>
        </div>
    </div>
[/#macro]

[#macro printParametersTable parameters]
    <table class="table" style="font-size: 90%">
        <thead>
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Description</th>
                <th class="text-center">Required</th>
                <th>Example</th>
                <th>Flags</th>
            </tr>
        </thead>
        <tbody>
        [#list parameters as p]
            <tr>
                <th class="text-monospace">${p.name}</th>
                <td>${p.type}</td>
                <td>${p.descriptionHtml}</td>
                <td class="text-center">
                [#if p.required]
                    <span class="badge badge-success">Y</span>
                [#else]
                    <span class="badge badge-danger">N</span>
                [/#if]
                </td>
                <td class="text-monospace">${p.example}</td>
                <td>
                    [#if p.pattern?has_content][@printParameterBadge "Pattern" p.pattern/][/#if]
                    [#if p.default?has_content][@printParameterBadge "Default" p.default/][/#if]
                    [#if p.multiple][@printParameterBadge "Multiple"/][/#if]
                    [#if p.minValue?has_content][@printParameterBadge "Minimum" p.minValue/][/#if]
                    [#if p.maxValue?has_content][@printParameterBadge "Maximum" p.maxValue/][/#if]
                    [#if p.allowedValues?has_content][@printParameterBadge "Allowed" p.allowedValues?join(", ")/][/#if]
                </td>
            </tr>
        [/#list]
        </tbody>
    </table>
[/#macro]

[#macro printRequestParameters type parameters]
    <h4>${type} Parameters</h4>
    [@printParametersTable parameters/]
[/#macro]

[#macro printResponseHeaders headers]
    <p class="font-weight-bold">Headers</p>
    [@printParametersTable headers/]
[/#macro]

[#macro printSecuritySchemeParameterRow scheme parameterType parameter]
    <tr>
        <th class="text-monospace">${scheme.name}</th>
        <td>${scheme.type}</td>
        <td>${parameterType}</td>
        <td class="text-monospace">${parameter.name}</td>
        <td class="text-monospace">[#if parameter.pattern?has_content]${parameter.pattern}[/#if]</td>
    </tr>
[/#macro]

[#macro printSecurityScheme scheme]
    [#if scheme.queryParameters?has_content]
        [#list scheme.queryParameters as p][@printSecuritySchemeParameterRow scheme "Query" p/][/#list]
    [/#if]
    [#if scheme.headers?has_content]
        [#list scheme.headers as p][@printSecuritySchemeParameterRow scheme "Header" p/][/#list]
    [/#if]
[/#macro]

[#macro printSecuritySchemes schemes]
    <h4>Security</h4>
    <div>
        <table class="table" style="font-size: 90%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Parameter Type</th>
                    <th>Parameter Name</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            [#list schemes as scheme][@printSecurityScheme scheme/][/#list]
            </tbody>
        </table>
    </div>
[/#macro]

[#macro printBody scope methodId body]
    [#assign requestId = utils.makeId(methodId + "-" + scope + "-" + body.contentType)/]
    <div class="row p-2 border-bottom">
        <div class="col-10 text-monospace font-weight-bold">${body.contentType}</div>
        <div class="col-1">
            [#if body.schema?has_content]
                [@printCodeDialog utils.makeId(requestId + "-schema") scope "Schema" body.schema/]
            [/#if]
        </div>
        <div class="col-1">
            [#if body.example?has_content]
                [@printCodeDialog utils.makeId(requestId + "-example") scope "Example" body.example/]
            [/#if]
        </div>
    </div>
[/#macro]

[#macro printRequests methodId requests]
    <h4>Request Body</h4>
    <div class="container-fluid mb-3">
        [#list requests as request][@printBody "Request" methodId request/][/#list]
    </div>
[/#macro]

[#macro printResponse methodId response]
    [#assign responseId = utils.makeId(methodId + "-responses-" + response.status)/]
    [#if response.status < 400][#assign color="success"/][#else][#assign color="danger"/][/#if]
    <div class="container-fluid border border-${color} mb-3">
        <div class="row p-3 hover-highlight" onclick="toggleVisibility('#${responseId}')">
            <div class="container-fluid">
                <div class="row">
                    <h5>${response.status} ${response.statusName}</h5>
                </div>
                [#if response.description?has_content]
                    <div class="row text-secondary font-weight-bold">
                        ${response.descriptionHtml}
                    </div>
                [/#if]
            </div>
        </div>
        <div id="${responseId}" class="row invisible d-none">
            <div class="container-fluid">
                <div class="row p-3 border-top">
                    <div class="container-fluid">
                        [#if response.headers?has_content][@printResponseHeaders response.headers/][/#if]
                        [#if response.bodies?has_content]
                            [#list response.bodies as body]
                                [@printBody "Response" responseId body/]
                            [/#list]
                        [/#if]
                    </div>
                </div>
            </div>
        </div>
    </div>
[/#macro]

[#macro printResponses methodId responses]
    <h4>Responses</h4>
    <div>
        [#list responses as response][@printResponse methodId response/][/#list]
    </div>
[/#macro]

[#macro printResource item]
    <div class="container-fluid mt-3 mb-3">
        <div class="row">
            <h3>${item.displayName}</h3>
        </div>
        <div class="row text-secondary">
            ${item.descriptionHtml}
        </div>
    </div>
    [#list item.methods as method]
        [@printMethod item.path method item.uriParameters/]
    [/#list]
    [#list item.resources as subResource]
        [@printSubResource subResource/]
    [/#list]
[/#macro]

[#macro printSubResource item]
    [#list item.methods as method]
        [@printMethod item.path method item.uriParameters/]
    [/#list]
    [#list item.resources as resource]
        [@printSubResource resource/]
    [/#list]
[/#macro]

[#macro printMethod path item uriParameters]
    [#assign methodId = utils.makeId(item.method + '-' + path)/]
    [#assign methodColour = utils.getMethodColour(item.method)/]
    <div class="container-fluid border shadow-sm mb-3">
        <div class="row">
            <div class="container-fluid">
                <div class="row hover-highlight" onclick="toggleVisibility('#${methodId}')">
                    <div class="col-1 bg-${methodColour} text-white p-2 font-weight-bold text-center d-table" style="height: 60px">
                        <div class="d-table-cell align-middle text-center">
                            ${item.method?upper_case}
                        </div>
                    </div>
                    <div class="col-11 text-left p-2 text-primary font-weight-bold">
                        <div>${path}[#if item.deprecated]&nbsp;<span class="badge badge-warning">DEPRECATED</span>[/#if]</div>
                        [#if item.description?has_content]
                        <div class="text-secondary font-weight-normal">${item.descriptionHtml}</div>
                        [/#if]
                    </div>
                </div>
                <div id="${methodId}" class="row invisible d-none">
                    <div class="p-3 border-top">
                        [#if uriParameters?has_content][@printRequestParameters "Path", uriParameters/][/#if]
                        [#if item.queryParameters?has_content][@printRequestParameters "Query", item.queryParameters/][/#if]
                        [#if item.headers?has_content][@printRequestParameters "Header" item.headers/][/#if]
                        [#if item.security?has_content][@printSecuritySchemes item.security/][/#if]
                        [#if item.requests?has_content][@printRequests methodId item.requests/][/#if]
                        [#if item.responses?has_content][@printResponses methodId item.responses/][/#if]
                    </div>
                </div>
            </div>
        </div>
    </div>
[/#macro]