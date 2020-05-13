export function digits(dd) {
  const numOfDecimal =
    Math.floor(dd) === dd ? 0 : dd.toString().split('.')[1].length || 0
  return numOfDecimal > 6 ? dd.toFixed(6) : dd
}
