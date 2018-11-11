[#ftl auto_esc=false]
[#import "documentation-tools.ftl" as documentation/]
[#import "api-tools.ftl" as api/]
[#import "utility-tools.ftl" as utils/]
<!DOCTYPE html>
<html>
    <head>
        <title>${application.title} - ${application.version}</title>

        <link rel="stylesheet" href="//stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <link rel="stylesheet" href="/apidocs/assets/css/theme.css"/>
        <link rel="stylesheet" href="/apidocs/assets/css/apidocs.css"/>
    </head>
    <body>
        <div class="d-none d-lg-block">
            <div class="border bg-primary pl-3 pr-3">
                <div class="row text-light">
                    <div class="col-sm-auto">
                        <h1>${application.title}</h1>
                    </div>
                </div>
                <div class="row text-secondary pb-2">
                    <div class="col-4">
                        Version ${application.version}
                    </div>
                    <div class="col-8 text-right">
                        ${application.baseUri}
                    </div>
                </div>
            </div>
            <div class="container-fluid">
                <div class="row">
                    <div class="col-2 border-right">
                        <ul class="nav flex-column nav-pills navbar-fixed-top" id="nav-menu" role="tablist">
                            <li class="nav-item mt-2"><h4 class="border-bottom">API</h4></li>
                            [#assign firstItem = true/]
                            [#list application.resources as resource]
                                [#assign apiId = utils.makeId(resource.displayName)/]
                                [#if firstItem]
                                    [#assign className = "nav-link active"/]
                                [#else]
                                    [#assign className = "nav-link"/]
                                [/#if]
                                <li class="nav-item"><a id="api-${apiId}-link" class="${className}" href="#api-${apiId}" data-toggle="list" role="tab" aria-controls="api-${apiId}">${resource.displayName}</a></li>
                                [#assign firstItem = false/]
                            [/#list]
                            [#if application.documentation?has_content]
                                <li class="nav-item mt-2"><h4 class="border-bottom">Documentation</h4></li>
                                [#list application.documentation as document]
                                    [#assign documentId = utils.makeId(document.title)/]
                                    <li class="nav-item"><a id="docuemtnation-${documentId}-link" class="nav-link" href="#documentation-${documentId}" data-toggle="list" role="tab" aria-controls="documentation-${documentId}">${document.title}</a></li>
                                [/#list]
                            [/#if]
                        </ul>
                    </div>
                    <div class="col-10">
                        <div class="tab-content" id="nav-content">
                            [#assign firstItem = true/]
                            [#list application.resources as resource]
                                [#assign apiId = utils.makeId(resource.displayName)/]
                                [#if firstItem]
                                    [#assign className = "tab-pane fade show active"/]
                                [#else]
                                    [#assign className = "tab-pane fade"/]
                                [/#if]
                                <div class="${className}" id="api-${apiId}" role="tabpanel" aria-labelledby="api-${apiId}-link">
                                    [@api.printResource resource/]
                                </div>
                                [#assign firstItem = false/]
                            [/#list]
                            [#if application.documentation?has_content]
                                [#list application.documentation as document]
                                    [#assign documentId = utils.makeId(document.title)/]
                                    <div class="tab-pane" id="documentation-${documentId}" role="tabpanel" aria-labelledby="documentation-${documentId}-link">
                                        <h1 class="font-weight-bold border-bottom">${document.title}</h1>
                                        <div id="${documentId}">
                                            ${document.contentHtml}
                                        </div>
                                    </div>
                                [/#list]
                            [/#if]
                        </div>
                    </div>
                </div>
            </div>

            [#--[#if application.documentation?has_content]--]
            [#--<div class="mt-3">--]
                [#--<ul class="nav nav-tabs" id="navTabs" role="tablist">--]
                    [#--<li class="nav-item">--]
                        [#--<a href="#api" class="nav-link active" id="api-tab" data-toggle="tab" role="tab" aria-controls="documentation" aria-selected="false">API</a>--]
                    [#--</li>--]
                    [#--<li class="nav-item">--]
                        [#--<a href="#documentation" class="nav-link" id="documentation-tab" data-toggle="tab" role="tab" aria-controls="documentation" aria-selected="false">DOCUMENTATION</a>--]
                    [#--</li>--]
                [#--</ul>--]
            [#--</div>--]
            [#--[/#if]--]
            [#--<div class="tab-content " id="navTabsContent">--]
                [#--<div id="api" class="tab-pane fade show active" role="tabpanel" aria-labelledby="api-tab">--]
                    [#--<div class="container-fluid">--]
                        [#--<div class="row">--]
                            [#--<div class="col-3 pb-3">--]
                                [#--<h4>Contents</h4>--]
                                [#--<div class="list-group" id="api-menu" role="tablist">--]
                                    [#--[#list application.resources as resource]--]
                                        [#--[#assign apiId = utils.makeId(resource.displayName)/]--]
                                        [#--<a href=#${apiId} class="list-group-item list-group-item-action" id="${apiId}-list" data-toggle="list" aria-controls="${apiId}">${resource.displayName}</a>--]
                                    [#--[/#list]--]
                                [#--</div>--]
                            [#--</div>--]
                            [#--<div class="col-9">--]
                                [#--<div class="tab-content" id="api-contents-tab">--]
                                [#--[#list application.resources as resource]--]
                                    [#--[#assign apiId = utils.makeId(resource.displayName)/]--]
                                    [#--<div class="tab-pane fade" id="${apiId}" role="tabpanel" aria-labelledby="${apiId}-list">--]
                                        [#--[@api.printResource resource/]--]
                                    [#--</div>--]
                                [#--[/#list]--]
                                [#--</div>--]
                            [#--</div>--]
                        [#--</div>--]
                    [#--</div>--]
                [#--</div>--]
                [#--[#if application.documentation?has_content]--]
                [#--<div id="documentation" class="tab-pane fade" role="tabpanel" aria-labelledby="documentation-tab">--]
                    [#--<div class="container">--]
                        [#--<div class="row">--]
                            [#--<div class="col-3">--]
                                [#--<h4>Contents</h4>--]
                                [#--<div class="list-group" id="documentation-menu" role="tablist">--]
                                    [#--[#list application.documentation as item]--]
                                    [#--[#assign documentId = utils.makeId(item.title)/]--]

                                    [#--<a href="#${documentId}" class="list-group-item list-group-item-action" id="${documentId}-list" data-toggle="list" aria-controls="${documentId}">${item.title}</a>--]
                                    [#--[/#list]--]
                                [#--</div>--]
                            [#--</div>--]
                            [#--<div class="col-9">--]
                                [#--<div class="tab-content" id="documentation-contents-tab">--]
                                    [#--[#list application.documentation as item]--]
                                        [#--[#assign documentId = utils.makeId(item.title)/]--]
                                        [#--<div class="tab-pane fade" id="${documentId}" role="tabpanel" aria-labelledby="${documentId}-list">--]
                                            [#--<h1 class="font-weight-bold border-bottom">${item.title}</h1>--]
                                            [#--<div id="${documentId}">--]
                                                [#--${item.contentHtml}--]
                                            [#--</div>--]
                                        [#--</div>--]
                                    [#--[/#list]--]
                                [#--</div>--]
                            [#--</div>--]
                        [#--</div>--]

                    [#--</div>--]
                [#--</div>--]
                [#--[/#if]--]
            [#--</div>--]
        </div>
        <div class="d-block d-lg-none">
            <div class="container border bg-primary">
                <div class="row text-light">
                    <div class="col-sm-auto">
                        <h1>${application.title}</h1>
                    </div>
                </div>
                <div class="row text-secondary pl-3 pb-2">
                    Version ${application.version}
                </div>
            </div>
            <div class="container mt-3 text-center">
                <div class="text-secondary">
                    <h3>This page is currently only designed<br/>to be viewed on desktops.</h3>
                </div>
            </div>
        </div>

        <script src="//code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="//stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
        <script src="/apidocs/assets/js/apidocs.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                bootstrap();
            });
        </script>
    </body>
</html>