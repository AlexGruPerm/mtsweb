function ajax_get(url, callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            console.dir('responseText:' + xmlhttp.responseText);
            try {
                var data = JSON.parse(xmlhttp.responseText);
            } catch(err) {
                console.dir(err.message + " in " + xmlhttp.responseText);
                return;
            }
            callback(data);
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

document.addEventListener("DOMContentLoaded", function(){
var list = document.getElementsByClassName('patt_search_row_class');
for (var i = 0; i < list.length; i++) {
     list[i].addEventListener("click",
     function(event){
      console.dir("Selected line TR id="+event.currentTarget.id);
       var partsOfStr = event.currentTarget.id.split('-');
       var tickerId  = partsOfStr[3];
       var widthSec  = partsOfStr[4];
       var bPattCnt  = partsOfStr[5];
       var bMaxTsEnd = partsOfStr[6];
      console.dir("tickerId="+tickerId);
      console.dir("widthSec="+widthSec);
      console.dir("bPattCnt="+bPattCnt);
      console.dir("bMaxTsEnd="+bMaxTsEnd);
      console.dir("------------------------");
      //---------------------------------------------------------------
        ajax_get('/getbars/'+tickerId+'/'+widthSec+'/'+bPattCnt+'/'+bMaxTsEnd, function(data) {
          console.dir("JSON data.rows="+data.length);
          document.getElementById("div-test").innerHTML = data;

           for (var i=0; i < data.length; i++) {
             console.dir("i=["+i+"]  x="+data[i]["x"]+" y="+data[i]["y"]);
           }

        });
      //---------------------------------------------------------------
      }
    );
  }
});