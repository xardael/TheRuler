<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>${rc.getMessage('appName')}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="${rc.getMessage('appSlogan')}">
    <meta name="author" content="Peter Gren">
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">
    <link href="${rc.contextPath}/res/css/bootstrap.min.css" rel="stylesheet">
    <link href="${rc.contextPath}/res/css/bootstrap-responsive.min.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      td {
        vertical-align: middle !important;
      }
    </style>
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->    
  </head>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">

        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="${rc.contextPath}/">${rc.getMessage('appName')}</a>

          <p class="navbar-text" style="display: inline;">${rc.getMessage('appSlogan')}</p>
          <div class="nav-collapse collapse pull-right">

            <ul class="nav">
              <li><a href="${rc.contextPath}/">${rc.getMessage('home')}</a></li>
              <li><a href="https://github.com/xardael/TheRuler/wiki">${rc.getMessage('help')}</a></li>
              <li><a href="https://github.com/xardael/TheRuler">${rc.getMessage('gitHub')}</a></li>
            </ul>
          </div><!--/nav-collapse -->
        </div> <!--/container -->
      </div> <!--/navbar-inner -->
    </div> <!--/navbar -->
      
    <div class="container">