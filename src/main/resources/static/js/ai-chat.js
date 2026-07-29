class AIChat {

    constructor(config) {
        this.endpoint = config.endpoint;
        this.storageKey = config.storageKey;
        this.cidKey = config.cidKey;

        this.chatBox = document.getElementById("chat-box");
        this.input = document.getElementById("input");

        this.cid = this.getCID();
        this.sessions = this.load();

        this.renderSidebar();
        this.loadChat();
    }

    getCID() {
        let cid = localStorage.getItem(this.cidKey);
        if (!cid) {
            cid = Date.now().toString();
            localStorage.setItem(this.cidKey, cid);
        }
        return cid;
    }

    load() {
        return JSON.parse(localStorage.getItem(this.storageKey) || "{}");
    }

    save() {
        localStorage.setItem(this.storageKey, JSON.stringify(this.sessions));
    }

    session() {
        if (!this.sessions[this.cid]) {
            this.sessions[this.cid] = {
                cid: this.cid,
                title: "New Chat",
                messages: []
            };
        }
        return this.sessions[this.cid];
    }

    async send(text) {

        if (!text.trim()) return;

        const s = this.session();

        s.messages.push({ role: "user", text });

        // Update chat title using first question
        if (s.messages.length === 1) {

            s.title = text.length > 25
                ? text.substring(0,25) + "..."
                : text;

            this.save();
        }

        this.render("user", text);

        this.showThinking();
        const res = await fetch(`${this.endpoint}?cid=${this.cid}&prompt=${encodeURIComponent(text)}`);

        const data = await res.text();

        this.hideThinking();
        this.fireworks();

        s.messages.push({ role: "bot", text: data });

        this.save();

        this.type(data);

        this.renderSidebar();
    }

    render(role, text) {
        const div = document.createElement("div");
        div.className = role === "user" ? "user-message" : "bot-message";
        div.innerHTML = this.formatResponse(text);
        this.chatBox.appendChild(div);
        this.chatBox.scrollTop = this.chatBox.scrollHeight;
    }

async type(text) {

    const div = document.createElement("div");

    div.className = "bot-message";

    this.chatBox.appendChild(div);


    let index = 0;


    while(index < text.length) {

        div.innerHTML = this.formatResponse(
            text.substring(0,index)
        );


        index++;


        await new Promise(resolve =>
            setTimeout(resolve,8)
        );


        this.chatBox.scrollTop =
            this.chatBox.scrollHeight;
    }
}

formatResponse(text){

    return text

        // markdown bold
        .replace(
            /\*\*(.*?)\*\*/g,
            "<strong>$1</strong>"
        )

        // newline handling
        .replace(/\n\n/g,"<br><br>")
        .replace(/\n/g,"<br>");
}

    renderSidebar() {
        const side = document.getElementById("history");
        side.innerHTML = "<h3>Chats</h3>";

        Object.values(this.sessions).forEach(s => {
            const d = document.createElement("div");
            d.className = "history-item";
            d.innerText = s.title;

            d.onclick = () => {
                this.cid = s.cid;
                this.loadChat();
            };

            side.appendChild(d);
        });
    }

    loadChat() {
        const s = this.session();
        this.chatBox.innerHTML = "";
        s.messages.forEach(m => this.render(m.role, m.text));
    }

    newChat() {
        this.cid = Date.now().toString();
        localStorage.setItem(this.cidKey, this.cid);
        this.loadChat();
        this.renderSidebar();
    }

    showThinking(){

        const div=document.createElement("div");

        div.id="thinking";

        div.className="bot-message thinking";


        div.innerHTML=`

            <span></span>
            <span></span>
            <span></span>

            <label>
            AI is thinking...
            </label>

        `;


        this.chatBox.appendChild(div);

        this.chatBox.scrollTop =
            this.chatBox.scrollHeight;

    }



    hideThinking(){

        const el=document.getElementById("thinking");

        if(el)
            el.remove();

    }

    fireworks(){

        const container =
        document.createElement("div");


        container.className="fireworks";


        container.innerHTML="🎆✨🎉";


        document.body.appendChild(container);


        gsap.fromTo(
            container,
            {
                scale:0,
                opacity:0
            },
            {
                scale:2,
                opacity:1,
                duration:0.5,
                y:-100,
                onComplete:()=>{
                    container.remove();
                }
            }
        );

    }
}