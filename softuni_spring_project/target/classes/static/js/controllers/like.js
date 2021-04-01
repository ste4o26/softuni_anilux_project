window.addEventListener("load", () => {
    const likeSubmit = document.getElementById("like");
    likeSubmit.addEventListener("click", (event) => likeHandler(event))
});

async function likeHandler(event) {
    event.preventDefault();
    const url = event.target.parentElement.action;
    let isSuccessful;
    let message;

    try {
        let data = await like(url);
        isSuccessful = true;
        message = `You have successfully liked ${data.name}`;
    } catch (e) {
        isSuccessful = false;
        message = "You have already liked this anime!";
    }

    renderMessage(message, isSuccessful);
}

async function like(url) {
    const csrfToken = document.getElementById("like").previousElementSibling.value;

    const response = await fetch(url, {
        method: "POST",
        headers: {"X-CSRF-TOKEN": csrfToken}
    });
    const data = await response.json();

    for (const property in data) {
        if (data.hasOwnProperty(property)) {
            if (data[property] !== null) {
                return data;
            }
        }
    }

    throw new Error();
}

function renderMessage(message, isSuccessful) {
    const small = document.getElementsByTagName("small")[0];
    small.textContent = message;

    if (isSuccessful) {
        small.style.color = "lightgreen";
    } else {
        small.style.color = "lightcoral";
    }
}