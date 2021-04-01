window.addEventListener("load", () => {
    const showErrorButton = document.getElementById("display-error-button");
    showErrorButton.addEventListener("click", showErrorMessageHandler);
});

function showErrorMessageHandler() {
    document.getElementById("error-message").style.visibility="visible";
}
