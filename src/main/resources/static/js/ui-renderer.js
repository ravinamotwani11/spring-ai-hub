const UI = {

    chatBox: document.getElementById("chat-box"),

    renderMessage(role, text) {
        const div = document.createElement("div");
        div.className = role === "user" ? "user-msg" : "bot-msg";
        div.innerText = text;

        this.chatBox.appendChild(div);

        gsap.from(div, {
            opacity: 0,
            y: 10,
            duration: 0.25
        });

        this.scrollToBottom();
    },

    clearChat() {
        this.chatBox.innerHTML = "";
    },

    scrollToBottom() {
        this.chatBox.scrollTop = this.chatBox.scrollHeight;
    }
};