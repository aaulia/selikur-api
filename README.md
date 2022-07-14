# Selikur API
This is an unofficial API for Cineplex 21 website. This is just a hobby project for learning kotlin on the server side.

## Dependencies
* [Ktor](https://ktor.io/)
* [skrape{it}](https://github.com/skrapeit/skrape.it)

## API Endpoints
| Endpoint                              | Method | Type   | Description                                                 |
|:--------------------------------------|:------:|:------:|:------------------------------------------------------------|
| `/movies`                             | `GET`  | `JSON` | Show currently playing movies                               |
| `/movies/upcoming`                    | `GET`  | `JSON` | Show upcoming movies                                        |
| `/movies/{movieId}`                   | `GET`  | `JSON` | Show specific movie details                                 |
| `/movies/{movieId}/theaters/{cityId}` | `GET`  | `JSON` | Show theaters that play specific movie in specific city     |
| `/cities`                             | `GET`  | `JSON` | Show cities that have Cineplex 21 theaters                  |
| `/cities/{cityId}`                    | `GET`  | `JSON` | Show list of theaters in specific city                      |
| `/theaters/{theaterId}`               | `GET`  | `JSON` | Show specific theater details (address, schedules, etc.)    |

## Caching
To ease the load on the source website, this api implements an in-memory caching for each _unique_ endpoint. The cache will have `maxAge` of `3600` seconds or an hour

## Payload Samples
### `/movies`, `/movies/upcoming`
```
[
    {
        "id": "12IVAA",
        "title": "IVANNA",
        "image": "https://web3.21cineplex.com/movie-images/12IVAA.jpg",
        "rating": "D17+",
        "type": "2D"
    },
    .
    .
    .
    {
        "id": "20TGMK",
        "title": "TOP GUN: MAVERICK",
        "image": "https://web3.21cineplex.com/movie-images/20TGMK.jpg",
        "rating": "SU",
        "type": "2D"
    }
]
```
### `/movies/{movieId}`
```
{
	"id": "21BPHE",
	"title": "THE BLACK PHONE",
	"image": "https://web3.21cineplex.com/movie-images/21BPHE.jpg",
	"rating": "D17+",
	"type": "2D",
	"genre": [
		"Horror",
		"Thriller"
	],
	"duration": "103 Minutes",
	"trailer": "https://web3.21cineplex.com/movie-trailer/21BPHE.mp4",
	"summary": "Setelah diculik dan dikunci di ruang bawah tanah, seorang anak laki-laki berusia 13 tahun mulai menerima panggilan telepon yang terputus dari korban-korban sebelumnya.",
	"producers": [
		"Jason Blum",
		"C. Robert Cargill",
		"Scott Derrickson"
	],
	"directors": [
		"Scott Derrickson"
	],
	"writers": [
		"Scott Derrickson",
		"C. Robert Cargill"
	],
	"casts": [
		"Ethan Hawke",
		"Jeremy Davies",
		"Mason Thames",
		"James Ransone",
		"Madeleine Mcgraw",
		"Kellan Rhude",
		"Banks Repeta",
		"Braxton Alexander",
		"J. Gaven Wilde"
	],
	"distributors": [
		"Universal Pictures"
	]
}
```
### `/cities`
```
[
    {
        "id": "32",
        "name": "AMBON"
    },
    .
    .
    .
    {
        "id": "23",
        "name": "YOGYAKARTA"
    }
]
```
### `/movies/{movieId}/theaters/{cityId}`, `/cities/{cityId}`
```
{
    "id": "4",
    "name": "BEKASI",
    "xxi": [
        {
            "id": "BKSCICI",
            "name": "CIPUTRA CIBUBUR XXI"
        },
        .
        .
        .
        {
            "id": "BKSTRJU",
            "name": "TRANSPARK MALL JUANDA XXI"
        }
    ],
    "premiere": [
        {
            "id": "BKSPRCC",
            "name": "CIPUTRA CIBUBUR PREMIERE"
        },
        .
        .
        .
        {
            "id": "BKSPRSB",
            "name": "SUMMARECON MAL BEKASI PREMIERE"
        }
    ],
    "imax": [
        {
            "id": "BKSIXSB",
            "name": "SUMMARECON MAL BEKASI IMAX"
        },
        .
        .
        .
    ]
}
```
### `/theaters/{theaterId}`
```
{
    "id": "BKSMEBE",
    "name": "MEGA BEKASI XXI",
    "address": "MEGA BEKASI HYPER MALL LT. 5, JL. A . YANI NO.1",
    "phoneNo": "(021) 889 666 21",
    "longlat": [
        "-6.249664",
        "106.993178"
    ],
    "schedules": [
        {
            "movie": {
                "id": "12IVAA",
                "title": "IVANNA",
                "image": "https://web3.21cineplex.com/movie-images/12IVAA.jpg",
                "rating": "D17+",
                "type": "2D"
            },
            "duration": "103 Minutes",
            "price": "Rp 35,000",
            "date": "14-07-2022",
            "time": [
                "12:30",
                "14:45",
                "17:00",
                "19:15",
                "21:30"
            ]
        },
        {
            "movie": {
                "id": "21MAUN",
                "title": "THE MAURITANIAN",
                "image": "https://web3.21cineplex.com/movie-images/21MAUN.jpg",
                "rating": "D17+",
                "type": "2D"
            },
            "duration": "129 Minutes",
            "price": "Rp 35,000",
            "date": "14-07-2022",
            "time": [
                "13:00",
                "15:40",
                "18:20",
                "21:00"
            ]
        },
        {
            "movie": {
                "id": "11PPEA",
                "title": "PERJALANAN PERTAMA",
                "image": "https://web3.21cineplex.com/movie-images/11PPEA.jpg",
                "rating": "SU",
                "type": "2D"
            },
            "duration": "112 Minutes",
            "price": "Rp 35,000",
            "date": "14-07-2022",
            "time": [
                "13:45",
                "16:05",
                "18:25",
                "20:45"
            ]
        },
        {
            "movie": {
                "id": "22TLAT",
                "title": "THOR: LOVE AND THUNDER",
                "image": "https://web3.21cineplex.com/movie-images/22TLAT.jpg",
                "rating": "R13+",
                "type": "2D"
            },
            "duration": "119 Minutes",
            "price": "Rp 35,000",
            "date": "14-07-2022",
            "time": [
                "11:45",
                "12:15",
                "12:45",
                "13:15",
                "14:15",
                "14:45",
                "15:15",
                "15:45",
                "16:45",
                "17:15",
                "17:45",
                "18:15",
                "19:15",
                "19:45",
                "20:15",
                "20:45",
                "21:45"
            ]
        },
        {
            "movie": {
                "id": "20MTRG",
                "title": "MINIONS 2: THE RISE OF GRU",
                "image": "https://web3.21cineplex.com/movie-images/20MTRG.jpg",
                "rating": "SU",
                "type": "2D"
            },
            "duration": "87 Minutes",
            "price": "Rp 35,000",
            "date": "14-07-2022",
            "time": [
                "12:00",
                "12:30",
                "14:00",
                "14:30",
                "16:00",
                "16:30",
                "18:00",
                "18:30",
                "20:00",
                "20:30"
            ]
        },
        {
            "movie": {
                "id": "21BPHE",
                "title": "THE BLACK PHONE",
                "image": "https://web3.21cineplex.com/movie-images/21BPHE.jpg",
                "rating": "D17+",
                "type": "2D"
            },
            "duration": "103 Minutes",
            "price": "Rp 35,000",
            "date": "14-07-2022",
            "time": [
                "13:35",
                "18:50"
            ]
        },
        {
            "movie": {
                "id": "22JWDN",
                "title": "JURASSIC WORLD DOMINION",
                "image": "https://web3.21cineplex.com/movie-images/22JWDN.jpg",
                "rating": "R13+",
                "type": "2D"
            },
            "duration": "147 Minutes",
            "price": "Rp 35,000",
            "date": "14-07-2022",
            "time": [
                "15:50",
                "21:05"
            ]
        }
    ]
}
```
