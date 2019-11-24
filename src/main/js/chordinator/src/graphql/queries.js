import gql from 'graphql-tag'

export const GET_ALL_MUSIC_ENUMS = gql`
  query getMusicEnums {
    getMusicEnums {
     notes
    }
  }
`;