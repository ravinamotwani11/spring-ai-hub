function initEffects() {

    particlesJS("particles-js", {
        particles: {
                number: { value: 60 },
                size: { value: 3 },
                move: { speed: 1 },
                line_linked: { enable: true }
            }
    });

    gsap.from("#chat-box", {
        opacity: 0,
        y: 20,
        duration: 0.6
    });

    function launchCelebration(type){


    if(typeof confetti==="undefined")
    return;



    let colors;



    if(type==="tvd"){

    colors=[
    "#ff0000",
    "#800080"
    ];

    }


    if(type==="simpsons"){

    colors=[
    "#ffe600",
    "#00a2ff"
    ];

    }


    if(type==="fitness"){

    colors=[
    "#00ff88",
    "#00ffaa"
    ];

    }



    confetti({

    particleCount:120,

    spread:100,

    colors:colors

    });


    }
}