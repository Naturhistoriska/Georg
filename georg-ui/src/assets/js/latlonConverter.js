function truncate(n) {
  return n > 0 ? Math.floor(n) : Math.ceil(n)
}

export function latlon(dd, longOrLat) {
  let hemisphere = /^[WE]|(?:lon)/i.test(longOrLat)
    ? dd < 0
      ? 'W'
      : 'E'
    : dd < 0
    ? 'S'
    : 'N'

  const absDD = Math.abs(dd)
  const degrees = truncate(absDD)
  const minutes = truncate((absDD - degrees) * 60)
  const seconds = ((absDD - degrees - minutes / 60) * Math.pow(60, 0)).toFixed(
    0
  )

  let dmsArray = [degrees, minutes, seconds, hemisphere]
  return `${dmsArray[0]}°${dmsArray[1]}'${dmsArray[2]}" ${dmsArray[3]}`
}
