$(document).ready(function () {
	populateChannels();
})

requestChannel = (channelName)=> {
	ws = new WebSocket('ws://localhost:8080/AndroidNetToTv');
	(function() {
		ws.onopen = function(e) {
			console.log("AndroidNetToTv Socket connected!!!");
			ws.send(channelName);
		}
		ws.onmessage = function(evt) {
			console.log(evt.data);
			var player = videojs('androidnettotv-video');
			player.src({type: "application/x-mpegURL", src: evt.data});
			player.play();
		};
	})();
}

populateChannels = () => {
	channelsList.forEach((channel) => {
		let channelItem = $(`<img title='${channel.name}' src='${channel.logo}' />`);
		channelItem.click(() => {
			requestChannel(channelItem.attr('title'));
		});
		$('#channel-aria').append(channelItem);
	})
} 