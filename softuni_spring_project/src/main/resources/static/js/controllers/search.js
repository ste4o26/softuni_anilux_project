// import searchAnimes from "../infrastructure/search";

window.addEventListener('load', run);

function run() {
    const searchBar = document.getElementById("search");
    searchBar.addEventListener("input", () => displayFoundResult(searchBar.value));

    const foundResultContainer = document.getElementsByClassName("found-result-container")[0];
    foundResultContainer.addEventListener("click", (event) => autoFillInput(event, searchBar));

    const findButton = document.getElementById("search-link");
    findButton.addEventListener("click", () =>
        showErrorIfAnimeDoesNotExists(searchBar, foundResultContainer));
}

async function displayFoundResult(searchValue) {
    const foundResultContainer = document.getElementsByClassName("found-result-container")[0];
    const foundAnimes = await searchAnimes(searchValue);

    console.log(foundAnimes);

    foundResultContainer.innerHTML = foundAnimes
        .map((anime, index) => {
            return `<div class="single-found-result" id="${index}">
                    <img class="search-img" src="${anime.imageThumbnailUrl}"/>
                    <h4 class="search-title">${anime.name}</h4>
                    <h4 class="search-likes">${anime.likes} <i class="fas fa-heart"></i></h4>
                </div>`
        }).join("");
}

function autoFillInput(event, searchBar) {
    let target = event.target;

    if (target.className === "search-img" || target.className === "search-title" || target.className === "search-likes") {
        target = target.parentElement;
    } else if (target.className !== "single-found-result") {
        return;
    }

    searchBar.value = target.getElementsByClassName("search-title")[0].textContent;

    const form = document.getElementById("search-link");
    form.href = `/animes/byTitle?title=${searchBar.value}`;
}

async function showErrorIfAnimeDoesNotExists(searchBar, foundResultContainer) {
    const foundAnimes = await searchAnimes(searchBar.value);
    if (foundAnimes.length === 0) {
        foundResultContainer.innerHTML = "<small>Not Found</small>";
    }
}

async function searchAnimes(searchValue) {
    if (searchValue.trim() === "") {
        return await new Promise(resolve => {
            resolve([]);
        });
    }

    const url = `/animes/search?search=${searchValue}`;
    const response = await fetch(url, {method: "GET"});

    return await response.json();
}
