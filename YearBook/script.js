const target = graduates.find(g => g.id === gradId);
if (target) {
target.signatures.push(note);
saveGraduates(graduates);
renderGraduates(document.getElementById("searchInput").value);
}
}
function escapeHtml(text) {
const div = document.createElement("div");
div.textContent = text;
return div.innerHTML;
}
document.getElementById("gradForm").addEventListener("submit", function (e) {
e.preventDefault();
const name = document.getElementById("name").value.trim();
const major = document.getElementById("major").value.trim();
const quote = document.getElementById("quote").value.trim();
const memory = document.getElementById("memory").value.trim();
const nextStep = document.getElementById("nextStep").value.trim();
const photoFile = document.getElementById("photo").files[0];
const saveEntry = (photoDataUrl) => {
const graduates = getGraduates();
graduates.unshift({
id: Date.now(),
name,
major,
photo: photoDataUrl,
quote,
memory,
nextStep,
signatures: []
});
saveGraduates(graduates);
e.target.reset();
renderGraduates(document.getElementById("searchInput").value);
};
if (photoFile) {
const reader = new FileReader();
reader.onload = () => saveEntry(reader.result);
reader.readAsDataURL(photoFile);
} else {
saveEntry("");
}
});
document.getElementById("searchInput").addEventListener("input", function (e) {
renderGraduates(e.target.value);
});
renderGraduates();