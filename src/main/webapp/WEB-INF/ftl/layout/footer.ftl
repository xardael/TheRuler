
      <hr>

      <footer>
        <p>&copy; Peter Gren 2013 | All rights reserved <!-- | <a href="#">Help</a> | <a href="#">Feedback</a> | <a href="#">GitHub</a> --></p>
      </footer>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="${rc.contextPath}/res/js/bootstrap.min.js"></script>
    <script src="${rc.contextPath}/res/js/bootbox.min.js"></script>
    
    
     <script type="text/javascript">
        
        var toggle = 0;
         
        $(document).ready(function() {
            //bootbox.confirm("Are you sure?", function(result) {
            //Example.show("Confirm result: "+result);
            
            $('#content').each(function() {
                $this = $(this);
                $this.data('defaultval', $this.val());
            });
        }); 
            
            //get popover working with attribute
            $(function () {
                $('body').popover({
                    selector: '[data-toggle="popover"]'
                });

                $('body').tooltip({
                    selector: 'a[rel="tooltip"], [data-toggle="tooltip"]'
                });
            });
            
            // Validate form fields in install form 
            $("#AinstallForm").blur(function(){
                alert("a");
                var isFormValid = true;
                $("#installForm .required input:text").each(function(){ 
                    if ($.trim($(this).val()).length == 0){
                        $(this).parent().addClass("error");
                        isFormValid = false;
                    } else {
                        $(this).parent().removeClass("error");
                    }
                });
                if (!isFormValid) alert("Please fill in all the required fields (highlighted in red)");
                return isFormValid;
            });
            
            $('[name="newGrammar"]').submit(function() {
                if ($.trim($('[name="name"]').val()).length == 0) {
                    bootbox.alert("Grammar name must not by emtpy.");
                    return false;
                }
            });
            
            $('[name="ruleAdd"]').submit(function() {
                if ($.trim($('[name="ruleId"]').val()).length == 0) {
                    bootbox.alert("Rule name must not by emtpy.");
                    return false;
                }
            });
            
            $(".noempty").closest("form").submit(function() {
                if ($.trim($('[name="name"]').val()).length == 0) {
                    bootbox.alert("Grammar name must not by emtpy.");
                    return false;
                }
            });
            
            $(".delete").click( function(e) {
                var href = $(this).attr('href');
                e.preventDefault();
                bootbox.confirm("Do you really want to delete selected item?", function(result) {
                    if (result == true) {
                        if (e.isDefaultPrevented()) {
                            window.location = href;
                        }
                    }
                }); 
                
            });
            
            $(".discard").click( function(e) {
                if ($('#content').val() != $('#content').data('defaultval')) {
                    bootbox.confirm("Do you really want to discard changes?", function(result) {
                        if (result == true) {
                            $('#content').val($('#content').data('defaultval'));
                        }
                    }); 

                    
                    
                }
            });
            
            $('#AinstallForm').submit(function() {
                if ($.trim($("input:first").val()).length == 0) {
                    $("input:first").parent().addClass("error");
                    $("#inputHost").attr('data-content','blah blah').attr('title','Chyba').popover('show');
                    $("#test").addClass("error");
                    $("#test span").removeClass("hidden");
                    $("#error").removeClass("hidden");
                    return false;
                }
                $("span").text("Not valid!").show().fadeOut(1000);
                return false;
                
                return false;
            });
            
            $('form.validate').submit(function() {
                var isFormValid = true;
                
                $("input.required", this).each(function(){ 
                    if ($.trim($(this).val()).length == 0){
                        $(this).closest('div.control-group').addClass('error');
                        $(".help-inline", $(this).parent()).text('${rc.getMessage("fillEmpty")}');
                        isFormValid = false;
                    } else {
                        $(this).closest('div.control-group').removeClass('error');
                        $(".help-inline", $(this).parent()).text('');
                    }
                });
                
                return isFormValid;
            });
            
            $('#collapseBtn').click(function() {
                if (toggle % 2 == 0) {
                    $('i', this).removeClass('icon-chevron-down');
                    $('i', this).addClass('icon-chevron-up');
                } else {
                    $('i', this).removeClass('icon-chevron-up');
                    $('i', this).addClass('icon-chevron-down');
                }
                
                toggle++;
                $(".mcollapse").collapse('toggle');
            });
            
        $('#validateXml').click(function() {
            var btn = $(this)
            btn.button('loading');
            $.ajax({
                type: "POST",
                url: "${rc.contextPath}/ajax/validateXml",
                data: { content: $('#content').val()}
                }).done(function( msg ) {
                    btn.button('reset');
                    if (msg == 'true') {
                        $('.modal-body').html('<div class="alert alert-success"><strong>Well done!</strong> The document is valid SRGS grammar.</div>');
                    } else {
                        $('.modal-body').html('<div class="alert alert-error"><strong>Error!</strong> The document is not valid.</div><p>' + msg + '</p>');
                    }
                    $('#myModal').modal('toggle');
            });
        });
          
    </script>

  </body>
</html>
