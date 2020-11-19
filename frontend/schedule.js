async function getapi(date) {
    const url = window.location.protocol + "//" + window.location.hostname + ":8080/" + "schedule/".concat(date);
    const response = await fetch(url);
    var data = await response.json();
    if (response) {
        hideloader();
        showtable(data);
    }
}

function hideloader() {
    document.getElementById('loading').style.display = 'none';
}

function showtable(data) {
    let tab =
        `<tr align="center">
          <th></th>
          <th>Name</th> 
          <th>Incoming Voyage number</th> 
          <th>Outgoing Voyage number</th> 
          <th>Berthing Time</th> 
          <th>Departure Time</th> 
          <th>Berth Number</th> 
          <th>Status</th> 
          <th>Change Count</th> 
          <th>Favourite</th>
          <th>Subscribe</th>
         </tr>`;

    for (let r of Object.values(data)) {

        if (r.berthN === undefined) {
            r.berthN = "-";
        }

        var favButton = '<button class="btn btn-outline-warning" onclick="favnotif(this)"> <i class="fa fa-star"></i></button>';
        var subButton = '<button class="btn btn-outline-danger" onclick="subnotif(this)"> <i class="fa fa-bell"></i></button>';

        var bthngdt = new Date(r.bthgDt);
        var oGtime = new Date(r.originalTime);
        diff = Math.abs(bthngdt.getTime() - oGtime.getTime());
        if (r.changeCount == 0) {
            diff = '<i class="fa fa-circle" style="color:green"></i>';
        }
        else if (diff < 3600000) {
            diff = '<i class="fa fa-circle" style="color:yellow"></i>';
        } else {
            diff = '<i class="fa fa-circle" style="color:red"></i>'
        }

        tab += `<tr align="center">
                <td>${diff}</td> 
                <td>${r.abbrVslM}</td> 
                <td>${r.inVoyN}</td> 
                <td>${r.outVoyN}</td>  
                <td style="font-size:12px">${r.bthgDt}</td> 
                <td style="font-size:12px">${r.unbthgDt}</td> 
                <td>${r.berthN}</td>   
                <td>${r.status}</td>   
                <td>${r.changeCount}</td>
                <td>${favButton}</td>
                <td>${subButton}</td>
                <td style="display:none;">${r.shiftSeqN}</td>
                </tr>`;
    }

    document.getElementById("results").innerHTML = tab;
}

async function favnotif(button) {
    var tabledata = button.parentElement.parentElement;
    var abbrVslM = tabledata.cells[1].innerHTML;
    var inVoyN = tabledata.cells[2].innerHTML;
    var shiftSeqN = tabledata.cells[11].innerHTML;
    var user = localStorage.getItem("user");

    const url = window.location.protocol + "//" + window.location.hostname + ":8080/" + "addfav?user=" + user + '&abbrVslM=' + abbrVslM + '&inVoyN=' + inVoyN + '&shiftSeqN=' + shiftSeqN;
    const response = await fetch(url, { method: 'POST' });
    if (response) {
        button.innerHTML = '<i class="fa fa-check"></i>';
    }
}

async function subnotif(button) {
    var tabledata = button.parentElement.parentElement;
    var abbrVslM = tabledata.cells[1].innerHTML;
    var inVoyN = tabledata.cells[2].innerHTML;
    var shiftSeqN = tabledata.cells[11].innerHTML;
    var user = localStorage.getItem("user");

    const url = window.location.protocol + "//" + window.location.hostname + ":8080/" + "addsub?user=" + user + '&abbrVslM=' + abbrVslM + '&inVoyN=' + inVoyN + '&shiftSeqN=' + shiftSeqN;
    const response = await fetch(url, { method: 'POST' });
    if (response) {
        button.innerHTML = '<i class="fa fa-check"></i>';
    }
}