let app;

function navigate(type) {

    console.log("CLICKED:", type);

    // RESET ALL PAGES
    document.querySelectorAll(".page")
        .forEach(p => p.classList.remove("active"));

    // ALWAYS HIDE AUDIO FIRST
    const audio = document.getElementById("audio-controls");
    if (audio) audio.style.display = "none";

    if (type === "tweets") {

        document.getElementById("tweet-page")
            .classList.add("active");

        applyTheme("tweets");

        return;
    }
    // MENU PAGE (SMART MENU AI)
    if (type === "menu") {

        const foodPage = document.getElementById("food-page");
        foodPage.classList.add("active");

        applyTheme("menu");

        // show audio ONLY here
        if (audio) audio.style.display = "block";

        const loading = document.getElementById("food-loading");
        if (loading) loading.style.display = "none";

        const result = document.getElementById("food-result");
        if (result) result.innerHTML = "";

        return;
    }
    if(type === "tweets") {


        document
        .getElementById("tweet-page")
        .classList.add("active");


        applyTheme("tweets");


        loadTweetHistory();


        return;

    }

    if (type === "rag") {

        document.getElementById("rag-page").classList.add("active");
        applyTheme("rag");

        loadRagHistory();

        return;
    }

    // CHAT PAGES ONLY
    const page = document.getElementById("chat-page");

    if (!page) {
        console.error("Chat page missing");
        return;
    }

    page.classList.add("active");

    const endpoints = {
        tvd: "/tvd",
        simpsons: "/simpsons",
        fitness: "/fitness"
    };

    // init AI only if needed
    app = new AIChat({
        endpoint: endpoints[type],
        storageKey: type + "-history",
        cidKey: type + "-cid"
    });

    applyTheme(type);

    console.log("NAVIGATED TO CHAT:", type);
}


// SEND MESSAGE
function sendMessage() {
    const input = document.getElementById("input");
    app.send(input.value);
    input.value = "";
}

// NEW CHAT
function newChat() {
    app.newChat();
}

// AUDIO FUNCTION
AIChat.prototype.playMenuAudio = function () {
    fetch("/menu/audio")
        .then(r => r.blob())
        .then(b => {
            const url = URL.createObjectURL(b);
            new Audio(url).play();
        });
};

// ENTER KEY HANDLER
function handleEnter(event) {
    if (event.key === "Enter") {
        event.preventDefault();
        sendMessage();
    }
}

// GO HOME
function goHome() {

    document.querySelectorAll(".page")
        .forEach(p => p.classList.remove("active"));

    document.getElementById("landing-page")
        .classList.add("active");

    document.body.className = "";

    // ensure audio is hidden on home too
    const audio = document.getElementById("audio-controls");
    if (audio) audio.style.display = "none";

    console.log("🏠 Returned to landing page");
}

// THEME SYSTEM
function applyTheme(type) {

    document.body.className = "";

    switch (type) {

        case "tvd":
            document.body.classList.add("theme-tvd");
            break;

        case "simpsons":
            document.body.classList.add("theme-simpsons");
            break;

        case "fitness":
            document.body.classList.add("theme-fitness");
            break;

        case "menu":
            document.body.classList.add("theme-food");
            break;

        case "tweets":
            document.body.classList.add("theme-tweets");
            break;

        case "rag":
            document.body.classList.add("theme-rag");
            break;
    }
}

// UPLOAD MENU
function uploadMenu() {

    const fileInput = document.getElementById("menuFile");
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a menu image");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    document.getElementById("food-loading").style.display = "block";

    fetch("/menu/upload", {
        method: "POST",
        body: formData
    })
    .then(res => res.json())
    .then(data => {

        console.log("FOOD RESPONSE:", data);

        document.getElementById("food-loading").style.display = "none";

        const foodPage = document.getElementById("food-result");

        // full reset
        foodPage.innerHTML = "";

        foodPage.innerHTML = `
            <div class="food-card">
                <h2>🍽 English Menu</h2>
                <div class="menu-text">${data.englishMenu}</div>
            </div>
        `;

    })
    .catch(err => {
        console.error(err);
        alert("Upload failed");
    });
}

// AUDIO API
function playMenuAudio() {

    console.log("🔊 Requesting menu audio...");

    fetch("/menu/audio")
        .then(async (res) => {

            console.log("Audio status:", res.status);

            if (!res.ok) {
                throw new Error("Audio API failed");
            }

            return res.blob();
        })
        .then(blob => {

            if (!blob || blob.size === 0) {
                throw new Error("Empty audio response");
            }

            const url = URL.createObjectURL(blob);
            const audio = new Audio(url);

            audio.onplay = () => console.log("🎧 Audio playing");
            audio.onerror = () => console.error("Audio playback error");

            audio.play()
                .catch(err => {
                    console.error("Browser blocked autoplay:", err);
                    alert("Click allowed, then try again.");
                });

        })
        .catch(err => {
            console.error("Audio error:", err);
            alert("Failed to load menu audio");
        });
}

function generateTweets() {

    const loader = document.getElementById("tweet-loader");
    const result = document.getElementById("tweet-results");

    // ALWAYS start hidden state safe
    if (loader) loader.style.display = "none";

    const originalTweet =
        document.getElementById("originalTweet").value;

    const topicHint =
        document.getElementById("topicHint").value;

    const emojiLevel =
        document.getElementById("emojiLevel").value;

    const modernizationLevel =
        document.getElementById("modernizationLevel").value;

    if (!originalTweet) {
        alert("Enter tweet first");
        return;
    }

    loader.style.display = "block";
    result.innerHTML = "";

    const url =
        `/tweets?originalTweet=${encodeURIComponent(originalTweet)}`
        + `&topicHint=${encodeURIComponent(topicHint)}`
        + `&emojiLevel=${emojiLevel}`
        + `&modernizationLevel=${modernizationLevel}`;

    fetch(url)
        .then(res => res.json())
        .then(data => {

            loader.style.display = "none";

            displayTweets(data.tweets);

            saveTweetHistory(originalTweet, data.tweets);
        })
        .catch(err => {
            loader.style.display = "none";
            console.error(err);
            alert("Tweet generation failed");
        });
}

function displayTweets(tweets){


const box =
document.getElementById("tweet-results");


box.innerHTML="";


tweets.forEach((t,index)=>{


box.innerHTML += `


<div class="tweet-result">


<h3>
✨ ${t.voice}
</h3>


<p>
${t.tweet}
</p>


</div>


`;


});


}

function saveTweetHistory(original,tweets){


let history =
JSON.parse(
localStorage.getItem("tweet-history")
|| "[]"
);



history.unshift({

original:original,

tweets:tweets,

time:new Date().toLocaleString()

});



localStorage.setItem(
"tweet-history",
JSON.stringify(history.slice(0,20))
);


loadTweetHistory();


}




function loadTweetHistory(){


const container =
document.getElementById("tweet-history");


if(!container)
return;



let history =
JSON.parse(
localStorage.getItem("tweet-history")
|| "[]"
);



container.innerHTML="";



history.forEach((item,index)=>{


container.innerHTML += `


<div class="tweet-history-item"
onclick="openTweetHistory(${index})">


🐦 ${item.original}


<br>

<small>
${item.time}
</small>


</div>


`;

});


}




function openTweetHistory(index){


let history =
JSON.parse(
localStorage.getItem("tweet-history")
|| "[]"
);



displayTweets(
history[index].tweets
);


}

function showFireworks(){


const fire=document.createElement("div");


fire.className="fireworks";


fire.innerHTML="🎉✨🎆";


document.body.appendChild(fire);



setTimeout(()=>{

fire.remove();

},2000);


}