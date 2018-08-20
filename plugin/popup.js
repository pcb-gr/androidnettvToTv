var port = chrome.runtime.connect({
	name: "Sample Communication"
});

document.addEventListener("DOMContentLoaded", function () {
	var watchBt = document.getElementById('watchBt');
	watchBt.onclick = function () {
		chrome.tabs.create({url: chrome.extension.getURL('./watching_page.html')});
	}
}, false);

