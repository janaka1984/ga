// GAME START USING CUSTOM DATA & POINTERS FOR BOTH MOUSE AND TOUCH DEVICES
// ACTION: start-game
// gameId: gametitle (or a specified ID based on the backend)

function pointerDownHandler(e) {
    // Process the pointerenter event
    // console.log(e);
    // console.log(e.checked);

    if (e.dataset.action === 'game-start') {
        // trigger games with their corresponding game ID, name, parameters etc.
//        console.log(e.dataset.gameId)
		//console.log(JSON.parse(window.localStorage.getItem('settingsParam')))
		settings = JSON.parse(window.localStorage.getItem('settingsParam'))
		settings['game_id'] = e.dataset.gameId
		
		//console.log('settings>> ',settings)

//		var btns = document.querySelector('.game-cover-container');
//		var btns = document.querySelectorAll('.control, .wrc9');
//
//        [].forEach.call(btns, function(btn) {
////          console.log(btn);
//          btn.addEventListener('click', function(event) {
//            console.log(e.dataset.gameId + " Clicked!");
//          })
//        })
//		clickEvent.addEventListener('click', event => {
//            console.log('-----------------------------------')
//            Android.jsCallback(e.dataset.gameId);
//
//        });
        var allButtons = document.querySelectorAll('#control, #wrc9, #wrc8');
        for (var i = 0; i < allButtons.length; i++) {
          allButtons[i].addEventListener('click', function() {
//            console.clear();
//            console.log("You clicked:", settings[0]);
            Android.jsCallback(JSON.stringify(settings[0]));
          });
        }
    }

    if (e.dataset.action === 'nav-toggle') {
        // toggleClass('sidebar-left', 'hidden');
    }
}
