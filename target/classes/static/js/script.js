let btn1 = document.getElementById('btn1');
let checkIn = document.getElementById('date1');
btn1.addEventListener('click', minDate);

function minDate(){
    let nowDate = new Date;
    if(checkIn.value < Date){

        alert("invalid date")
    }

}
