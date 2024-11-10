async function subscribe() {
  const form = document.getElementById("subscription_form")
  const data = new FormData(form)
  
  const response = await fetch("/subscribe", {
    method: "POST",
    body: data
  })

  const result = await response.json()
  const messageContainer = document.getElementById("subscription_result")

  messageContainer.classList = [result.successful ? "ok" : "error"]
  messageContainer.innerText = result.message
}