	$(function() {
		var options = {
			lines: {
				show: true
			},
			points: {
				show: true
			},
			xaxis: {
				tickDecimals: 0,
				tickSize: 1
			}
		};

		var data = [];
		$.plot("#placeholder", data, options);
		// Fetch one series, adding to what we already have
		var alreadyFetched = {};

		// Initiate a recurring data update
		$("button.dataUpdate").click(function () {
			data = [];
			alreadyFetched = {};
			$.plot("#placeholder", data, options);
			var iteration = 0;
			function fetchData() {
				++iteration;
				function onDataReceived(series) {

					// Load all the data in one pass; if we only got partial
					// data we could merge it with what we already have.

					data = [ series ];
					$.plot("#placeholder", data, options);
				}

				// Normally we call the same URL - a script connected to a
				// database - but in this case we only have static example
				// files, so we need to modify the URL.

				$.ajax({
					url: "data-eu-gdp-growth-" + iteration + ".json",
					type: "GET",
					dataType: "json",
					success: onDataReceived
				});

				if (iteration < 5) {
					setTimeout(fetchData, 1000);
				} else {
					data = [];
					alreadyFetched = {};
				}
			}
			setTimeout(fetchData, 1000);
		});
	});








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
var list = document.getElementsByClassName('patt-search-row-class');
for (var i = 0; i < list.length; i++) {
     list[i].addEventListener("click",function(){
     funcOnClick(event);
     }
     /*
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
      */
    );
  }
});

function funcOnClick(event) {
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

           paintBarsGraph();

           paintJQBarsGraph();

        });
      //---------------------------------------------------------------
}

/*
window.addEventListener("load", function(){
        console.dir("window.addEventListener - Load");
		var data = getRandomData('April 01 2017', 20);
		var ctx = document.getElementById("chart1").getContext("2d");
		//ctx.canvas.width = 350;
		//ctx.canvas.height = 350;
		new Chart(ctx, {
			type: 'candlestick',
			data: {
				datasets: [{
					label: "XYZ",
					data: data,
					fractionalDigitsCount: 5,
				}]
			},
			options: {
				tooltips: {
					position: 'nearest',
					mode: 'index',
				},
			},
		});
});
*/




function paintBarsGraph(){
        console.dir("function paintBarsGraph");
		var data = getRandomData('April 01 2017', 30);
		console.dir("data="+data);
		var ctx = document.getElementById("chart1").getContext("2d");
		//ctx.canvas.width = 350;
		//ctx.canvas.height = 350;
		new Chart(ctx, {
			type: 'candlestick',
			data: {
				datasets: [{
					label: "XYZ",
					data: data,
					fractionalDigitsCount: 5,
				}]
			},
			options: {
				tooltips: {
					position: 'nearest',
					mode: 'index',
				},
			},
		});
}


function paintJQBarsGraph(){
      console.dir("function paintJQBarsGraph");

			var button = $(this);

			// Find the URL in the link right next to us, then fetch the data

			var dataurl = "http://localhost:9000/getbars/3/30/10/1536833031";

			function onDataReceived(series) {

				// Extract the first coordinate pair; jQuery has parsed it, so
				// the data is now just an ordinary JavaScript object

				var firstcoordinate = "(" + series.data[0][0] + ", " + series.data[0][1] + ")";
				button.siblings("span").text("Fetched " + series.label + ", first point: " + firstcoordinate);

				// Push the new data onto our existing data array

				if (!alreadyFetched[series.label]) {
					alreadyFetched[series.label] = true;
					data.push(series);
				}

				$.plot("#placeholder", data, options);
			}

			$.ajax({
				url: dataurl,
				type: "GET",
				dataType: "json",
				success: onDataReceived
			});


}




