function successAlert(msg) {
  return `
    <div role="alert" class="alert alert-success">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      <span>${msg}</span>
    </div>
    `
}

function errorAlert(msg) {
  return `
    <div role="alert" class="alert alert-error">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      <span>${msg}</span>
    </div>
    `
}

function onSubmit(form) {
  subscribe(form);
  return false;
}

function displayResult(result) {
  const alertContainer = document.getElementById("alert_container")
  const toastContainer = document.createElement("div")
  toastContainer.innerHTML = result.successful ? successAlert(result.message) : errorAlert(result.message)
  const alertToast = toastContainer.firstElementChild

  console.log(alertContainer)
  console.log(alertToast)
  alertContainer.appendChild(alertToast)
  setTimeout(() => alertToast.remove(), 4000)
}

async function subscribe(form) {
  const buttons = Array.from(document.getElementsByClassName("btn-newsletter"))
  const data = new FormData(form)

  buttons.forEach(btn => btn.classList.remove("swap-active"));
  
  const response = await fetch("/subscribe", {
    method: "POST",
    body: data
  })
  
  buttons.forEach(btn => btn.classList.add("swap-active"));

  displayResult(await response.json())
}

async function unsubscribe() {
  const form = document.getElementById("subscription_form")
  const data = new FormData(form)
  
  const response = await fetch("/unsubscribe", {
    method: "POST",
    body: data
  })

  displayResult(await response.json())
}

window.addEventListener("DOMContentLoaded", () => {
  Array.from(document.getElementsByClassName("indeterminate"))
    .forEach(element => element.indeterminate = true)
})