function truncate(n) {
  return n > 0 ? Math.floor(n) : Math.ceil(n)
  // return Math.round(n)
}

function digits(dd) {
  const numOfDecimal =
    Math.floor(dd) === dd ? 0 : dd.toString().split('.')[1].length || 0
  return numOfDecimal > 6 ? dd.toFixed(6) : dd
}

export function latlon(dd, longOrLat, isDDM) {
  let hemisphere = /^[WE]|(?:lon)/i.test(longOrLat)
    ? dd < 0
      ? 'W'
      : 'E'
    : dd < 0
    ? 'S'
    : 'N'

  const absDD = Math.abs(dd)
  const degrees = truncate(absDD)
  // const minutes = truncate((absDD - degrees) * 60)
  const minutes = (absDD - degrees) * 60
  const seconds = ((absDD - degrees - minutes / 60) * Math.pow(60, 0)).toFixed(
    0
  )
  return isDDM
    ? `${degrees}°${digits(minutes)}' ${hemisphere}`
    : `${degrees}°${truncate(minutes)}'${seconds}" ${hemisphere}`

  // let dmsArray = [degrees, minutes, seconds, hemisphere]
  // return isDDM
  //   ? `${dmsArray[0]}°${dmsArray[1]}'${dmsArray[2]}" ${dmsArray[3]}`
  //   : `${dmsArray[0]}°${dmsArray[1]}'${dmsArray[2]}" ${dmsArray[3]}`
}
