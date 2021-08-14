if(!window.localStorage.getItem('navState')) window.localStorage.setItem('navState', 'dash')

document.querySelectorAll('.navItem').forEach(item => {
    item.addEventListener(

        'pointerdown', (e) => {

            // hide current local.storage nav state
            toggleClass(window.localStorage.getItem('navState'), 'hidden')
            // set local.storage nav state
            window.localStorage.setItem('navState', e.srcElement.classList[0])
            // show new local.storage nav state
            toggleClass(window.localStorage.getItem('navState'), 'hidden')
        }
    )
})