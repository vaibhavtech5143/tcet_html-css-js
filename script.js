console.log("before intializing vaibhav");

let submitBtn = document.getElementById("submitBtn");
let emailInput = document.getElementById("username");


console.log(submitBtn);
console.log(emailInput);



let resultCont = document.getElementById("resultCont");



// Email Validator 

// pahekla email collect
// then api call
// then result show



submitBtn.addEventListener("click",async(vaibhav)=>{
    vaibhav.preventDefault()
    // console.log("clicked")
    // let email = document.getElementById("username").value
    // console.log(email)

    // let apiKey = "ema_live_AxGfGx4LFQDj0R71O9vvfXNyRPdHFl6NeirabAYf"

    // let url = `https://api.emailvalidation.io/v1/info?apikey=${apiKey}&email=vaibhavsingh2633@gmail.com`

    // console.log(url);

    // let res = await fetch(url)
    // let result = await res.json()

    // console.log(result);

    // //  js frontend -> backend db save karna raha ga

    // let InnerHtmlData = ``;
    // for (let key in result) {
    //     console.log(result[key]);
    //     // a += 4   a = a+4;
    //     InnerHtmlData += `<div><strong>${key}:</strong> ${result[key]}</div>`;
    // }
    
    // resultCont.innerHTML = `<div>${InnerHtmlData}</div>`

    try {

        const sendingVariable =  await fetch('http://localhost:8080/submit',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `email=${encodeURIComponent("testing@gmail.com")}&name=${encodeURIComponent("vaibhav")}`
        })

        const testingResult = await sendingVariable.json()
        console.log(testingResult);
        

        
        
    } catch (error) {
        
    }


    // promise api call -> pending -> resolve -> fail


    
}
)




console.log("APi Result ka baad");

