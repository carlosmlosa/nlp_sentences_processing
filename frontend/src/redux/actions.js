export function getTranscript() {
  return {
    type: "GET_TRANSCRIPT",
  };
}

export function editTranscript(newTranscript) {
  return {
    type: "EDIT_TRANSCRIPT",
    newTranscript,
  };
}
