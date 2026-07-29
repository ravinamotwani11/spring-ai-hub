const RAG_CID_KEY = "rag-cid";
const RAG_HISTORY_KEY = "rag-history";

function getRagCid() {
    let cid = localStorage.getItem(RAG_CID_KEY);

    if (!cid) {
        cid = crypto.randomUUID();
        localStorage.setItem(RAG_CID_KEY, cid);
    }

    return cid;
}

async function askKnowledgeAI() {

    const input = document.getElementById("ragQuestion");
    const question = input.value.trim();

    if (!question) return;

    const loader = document.getElementById("rag-loader");
    loader.style.display = "block";

    const cid = getRagCid();

    try {

        const res = await fetch(
            `/ask?cid=${cid}&question=${encodeURIComponent(question)}`
        );

        const answer = await res.text();

        loader.style.display = "none";

        document.getElementById("rag-answer").innerHTML = `
            <div class="tweet-result">
                <h3>📚 Answer</h3>
                <p>${answer}</p>
            </div>
        `;

        saveRagHistory(question);

        input.value = "";

    } catch (e) {

        loader.style.display = "none";
        alert("Knowledge AI failed");
        console.error(e);
    }
}

function saveRagHistory(q) {

    let history =
        JSON.parse(localStorage.getItem(RAG_HISTORY_KEY) || "[]");

    history.unshift(q);

    history = [...new Set(history)];

    if (history.length > 10)
        history = history.slice(0, 10);

    localStorage.setItem(RAG_HISTORY_KEY, JSON.stringify(history));

    loadRagHistory();
}

function loadRagHistory() {

    const container = document.getElementById("rag-history");

    const history =
        JSON.parse(localStorage.getItem(RAG_HISTORY_KEY) || "[]");

    container.innerHTML = "";

    history.forEach(q => {

        container.innerHTML += `
            <div class="tweet-history-item"
                 onclick="askHistory('${q.replace(/'/g, "\\'")}')">
                📌 ${q}
            </div>
        `;
    });
}

async function askHistory(question) {

    document.getElementById("ragQuestion").value = question;

    document.getElementById("rag-loader").style.display = "block";

    const cid = getRagCid();

    const res = await fetch(
        `/askAgain?cid=${cid}&question=${encodeURIComponent(question)}`
    );

    const answer = await res.text();

    document.getElementById("rag-loader").style.display = "none";

    document.getElementById("rag-answer").innerHTML = `
        <div class="tweet-result">
            <h3>📚 Answer</h3>
            <p>${answer}</p>
        </div>
    `;
}