// all the settings in a tidy JSON format for easy adoption
let settingsJSON = [
    {
        profileName: 'default',
        settings: [
            {   
                type: 'section-headline',
                value: 'Server Settings'
            },
            {
                type: 'dropdown',
                settingName: 'server-protocol',
                value: 'rtsp',
                label: 'Protocol',
                description: 'Select the protol, with which you want to receive the gaming stream.'
            },
            {   
                type: 'input',
                settingName: 'server-ip',
                value: '127.0.0.1',
                label: 'Host',
                description: 'The IP address of your game server.'
            },
            {   
                type: 'input',
                settingName: 'port-server',
                value: '8554',
                label: 'Port',
                description: 'The port to use on game server.'
            },
            {   
                type: 'input',
                settingName: 'object-path',
                value: '/desktop',
                label: 'Object Path',
                description: 'Default value is \'\Desktop\''
            },
            { 
                type: 'checkbox',
                settingName: 'rtp-over-tcp',
                value: 'false',
                label: 'RTP over TCP',
                description: 'Stream RTP over TCP rather than UDP.'
            },
            {   
                type: 'section-headline',
                value: 'Controller Configuration'
            },
            {
                type: 'dropdown',
                settingName: 'controller-layout',
                value: 'empty',
                label: 'Controller layout',
                description: 'Select an on-screen controller layout.'
            },
            {
                type: 'checkbox',
                settingName: 'allow-controller-config',
                value: true,
                label: 'Enable',
                description: 'Enable custom controller configuration.'
            },
            {
                type: 'dropdown',
                settingName: 'controller-protocol',
                value: 'udp',
                label: 'Protocol',
                description: 'Select the protocol with which you want to send/receive controller signals.'
            },
            {   
                type: 'input',
                settingName: 'controller-port',
                value: '8554',
                label: 'Port',
                description: 'The port to use for the game controller signaling.'
            },
            {
                type: 'checkbox',
                settingName: 'relative-mouse-mode',
                value: false,
                label: 'Relative mouse mode',
                description: 'Use mouse mode relative to screen res of host.'
            },
            {   
                type: 'section-headline',
                value: 'Audio'
            },
            {
                type: 'dropdown',
                settingName: 'audio-channels',
                value: 2,
                label: 'Channels',
                description: 'Use mono or stereo audio output.'
            },
            {
                type: 'dropdown',
                settingName: 'audio-sample-rate',
                value: 44100,
                label: 'Sample rate',
                description: '44.1 kHz is the typical standard on most devices.'
            },
            {   
                type: 'section-headline',
                value: 'Additional Settings'
            },
            {
                type: 'checkbox',
                settingName: 'android-audio-decoder',
                value: false,
                label: 'Android Audio Decoder',
                description: 'Use Android\'s built-in audio decoder.'
            },
            {
                type: 'checkbox',
                settingName: 'android-video-decoder',
                value: false,
                label: 'Android Video Decoder',
                description: 'Use Android\'s built-in video decoder.'
            },
            {   
                type: 'checkbox',
                settingName: 'portrait-mode',
                value: false,
                label: 'Portrait Mode',
                description: 'Force streamer into portrait mode rather than switching to landscape orientation.'
            },
            {
                type: 'checkbox-slider',
                settingName: 'drop-late-video-frame',
                value: false,
                threshold: 100,
                label: 'Drop late video frame',
                description: 'Drop frames, when they are not delivered in time or just partly by the server.'
            },
            {
                type: 'checkbox-slider',
                settingName: 'watchdog-timeout',
                value: false,
                threshold: 100,
                label: 'Watchdog timeout',
                description: 'For how long should be tried to reconnect the stream.'
            }
        ]
    }
]



// save settings JSON to local html storage, if not already present
if(window.localStorage.getItem('settingsParam')) {
    settingsJSON = JSON.parse(window.localStorage.getItem('settingsParam'))
} else {
    window.localStorage.setItem('settingsParam', JSON.stringify(settingsJSON))
}



// custom event for settings data
const settingsUpdated = new Event('settingsUpdated', {
    bubbles: true,
    cancelable: true,
    composed: false
})
settingsUpdated.data = JSON.parse(window.localStorage.getItem('settingsParam'))

document.querySelector('#settings').addEventListener('settingsUpdated', (e) => {
    // console.log("custom event triggered")
    // console.log(e)
})


// add event listener to all dom elements of class setting
document.querySelectorAll('.setting').forEach(item => {
    item.addEventListener('change', (e) => {
            
            // console.log(e.srcElement)
            let element = e.srcElement

            if(element.type === 'checkbox') {
                // console.log('--- event listener for checkboxes ---')
                // console.log(e.srcElement.checked);
                // console.log(`Checkbox for ${element.name} is ${element.checked}`)
                // saveSettings has to be used here
                saveSettings(element.name, element.checked, undefined)
            }
            

            if(element.type === 'range') {
                // console.log('--- event listener for range ---')
                // console.log(e.srcElement.checked);
                // console.log(`Range slider for ${element.name} is ${element.value}`)
                // saveSettings has to be used here
                saveSettings(element.name, undefined, element.value)
            }


            if(element.type === 'select-one') {
                // console.log('--- event listener for select/dropdown ---')
                saveSettings(element.name, element.value, undefined)
            }


            if(element.type === 'text') {
                // console.log('--- event listener for text input ---')
                saveSettings(element.name, element.value, undefined)
            }

            settingsUpdated.data = JSON.parse(window.localStorage.getItem('settingsParam'))
            document.querySelector("#settings").dispatchEvent(settingsUpdated)
            
        }
    )
})


// generate settings UI based on JSON
// part of refactoring with react
const generateSettingsUI = () => {

        
}

// save changes to a specific setting in the settingsJSON
// and update the html 5local storage variable
const saveSettings = (settingName, value, threshold) => {

    console.log('SAVING ...')
    
    settingsJSON.forEach(profile => {

        // console.log(profile)
        profile.settings.forEach(setting => {
            
            if(setting.type != 'section-headline' && setting.settingName === settingName) {
                
                // console.log(setting.settingName)
                if(setting.settingName === settingName) {

                    // console.log('--- FOUND ---')
                    // console.log(setting.settingName)
                    
                    if(value != undefined || value != null) {
                        // console.log(`--- old value: ${setting.value} ---`)
                        setting.value = value
                        // console.log(`--- new and saved value: ${setting.value} ---`)
                        window.localStorage.setItem('settingsParam', JSON.stringify(settingsJSON))
                    } // else { console.log(`--- value unchanged ---`) }
                    
                    if(threshold != undefined || threshold != null) {
                        // threshold ? console.log(threshold) : console.log(`--- unchanged threshold: ${setting.threshold} ---`)
                        // console.log(`--- old threshold: ${setting.threshold} ---`)
                        setting.threshold = threshold
                        // console.log(`--- new threshold: ${setting.threshold} ---`)
                        window.localStorage.setItem('settingsParam', JSON.stringify(settingsJSON))
                    } // else { console.log(`--- threshold unchanged ---`) }
                }
            }
        })
    })

}


