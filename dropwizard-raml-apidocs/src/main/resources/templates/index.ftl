[#ftl]
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
            <div class="container border bg-primary">
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
            [#if application.documentation?has_content]
            <div class="mt-3">
                <ul class="nav nav-tabs justify-content-center" id="navTabs" role="tablist">
                    <li class="nav-item">
                        <a href="#api" class="nav-link active" id="api-tab" data-toggle="tab" role="tab" aria-controls="documentation" aria-selected="false">API</a>
                    </li>
                    <li class="nav-item">
                        <a href="#documentation" class="nav-link" id="documentation-tab" data-toggle="tab" role="tab" aria-controls="documentation" aria-selected="false">DOCUMENTATION</a>
                    </li>
                </ul>
            </div>
            [/#if]
            <div class="tab-content container" id="navTabsContent">
                <div id="api" class="tab-pane fade show active" role="tabpanel" aria-labelledby="api-tab">
                    [#list application.resources as resource][@api.printResource resource/][/#list]
                </div>
                [#if application.documentation?has_content]
                <div id="documentation" class="tab-pane fade" role="tabpanel" aria-labelledby="documentation-tab">
                    <div class="container mt-3 mb-3">
                        [#list application.documentation as item]
                        [@documentation.printDocumentation item/]
                        [/#list]
                    </div>
                </div>
                [/#if]
            </div>
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