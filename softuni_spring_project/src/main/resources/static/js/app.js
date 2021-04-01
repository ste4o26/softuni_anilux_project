window.addEventListener('load', run);

function run() {
    const [sidebarIcon, closeIcon] =
        [document.getElementsByClassName("sidebar-menu-icon")[0],
            document.getElementsByClassName("close-icon")[0]];

    if (sidebarIcon === null || sidebarIcon === undefined) {
        throw new Error("Sidebar not found!!!");
    }

    if (closeIcon === null || closeIcon === undefined) {
        throw new Error("Close icon not found!!!");
    }

    sidebarIcon.addEventListener('click', openSideBar);
    closeIcon.addEventListener('click', closeSideBar);
}

function openSideBar() {
    //TODO some error handling!!!
    const sidebar = document.getElementsByClassName("sidebar")[0];
    sidebar.style.width = "17em";
}

function closeSideBar() {
    const sidebar = document.getElementsByClassName("sidebar")[0];
    sidebar.style.width = "0";
}