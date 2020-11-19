function getFavourites(user) {
    const url = window.location.protocol + "//" + window.location.hostname + ":8080/" + "favourites?user=" + user
    const Http = new XMLHttpRequest();
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        hideloader();
        showFavourites(JSON.parse(Http.responseText))
    }
}

function hideloader() {
    document.getElementById('loading').style.display = 'none';
}

function showFavourites(data) {
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
         </tr>`;

    for (let r of Object.values(data)) {

        if (r.berthN === undefined) {
            r.berthN = "-";
        }

        var deleteButton = '<button class="btn btn-outline-danger" onclick="remove(this)"> <i class="fa fa-trash"></i></button>';

        var bthngdt = new Date(r.bthgDt);
        var oGtime = new Date(r.originalTime);
        let diff = Math.abs(bthngdt.getTime() - oGtime.getTime());
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
                <td>${deleteButton}</td>
                <td style="display:none;">${r.shiftSeqN}</td>
                </tr>`;
    }

    document.getElementById("results").innerHTML = tab;
}

async function remove(button) {
    var tabledata = button.parentElement.parentElement;
    var abbrVslM = tabledata.cells[1].innerHTML;
    var inVoyN = tabledata.cells[2].innerHTML;
    var shiftSeqN = tabledata.cells[10].innerHTML;
    var user = localStorage.getItem("user");

    const url = window.location.protocol + "//" + window.location.hostname + ":8080/" + "delfav?user=" + user + '&abbrVslM=' + abbrVslM + '&inVoyN=' + inVoyN + '&shiftSeqN=' + shiftSeqN;
    const response = await fetch(url, { method: 'POST' });
    if (response) {
        document.getElementById("results").deleteRow(tabledata.rowIndex);
    }
}