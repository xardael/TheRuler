
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
         
//        $(document).ready(function() {
//            bootbox.confirm("Are you sure?", function(result) {
//Example.show("Confirm result: "+result);
//}); 
            
            
            //get popover working with attribute
            $(function () {
                $('body').popover({
                    selector: '[data-toggle="popover"]'
                });

                $('body').tooltip({
                    selector: 'a[rel="tooltip"], [data-toggle="tooltip"]'
                });
            });
            
            $(".alert").alert()
            
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
          
    </script>

  </body>
</html>
