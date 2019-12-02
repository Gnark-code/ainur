import gql from 'graphql-tag'

export const GET_ALL_MUSIC_ENUMS = gql`
  query musicEnums {
    getMusicEnums {
     notes
     modes
     degrees
     subdivisions
     bassPatterns
    }
  }
`;