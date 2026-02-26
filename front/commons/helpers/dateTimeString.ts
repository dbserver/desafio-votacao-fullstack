export default function dateTimeString(date: string) {
  const d = new Date(date);

  return `${d.toLocaleDateString()} as ${d.toLocaleTimeString()}`;
}
