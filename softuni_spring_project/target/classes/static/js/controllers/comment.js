window.addEventListener("load", () => {
    const commentButton = document.getElementsByClassName("comment-submit")[0];
    commentButton.addEventListener("click", (event) => persistCommentHandler(event));
});

async function persistCommentHandler(event) {
    event.preventDefault();
    let [contentElement, form] = [document.getElementById("my-comment"), document.getElementsByClassName("comment-form")[0]];

    let content = contentElement.value;
    contentElement.value = "";

    try {
        let comment = await persistComment(form.action, content);

        contentElement = renderComment(comment);
        document.getElementsByClassName("all-comments")[0].appendChild(contentElement);
    } catch (e) {
        let small = document.getElementById("comment-error-message");
        small.textContent = "Comment must be at least 10 characters!";
    }
}

function renderComment(comment) {
    let singleCommentContainer = document.createElement("div");
    singleCommentContainer.className = "comment";

    let usernameParagraph = document.createElement("p");
    usernameParagraph.className = "comment-username";
    usernameParagraph.textContent = comment.userUsername;

    let contentParagraph = document.createElement("p");
    contentParagraph.className = "comment-content";
    contentParagraph.textContent = comment.content;

    singleCommentContainer.appendChild(usernameParagraph);
    singleCommentContainer.appendChild(contentParagraph);

    return singleCommentContainer;

}

async function persistComment(url, content) {
    const csrfToken = document.getElementsByName("_csrf")[0].value;

    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "text/plain",
            "X-CSRF-TOKEN": csrfToken
        },
        body: content
    });

    return await response.json();
}