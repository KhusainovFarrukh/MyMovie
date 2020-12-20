package academy.android.mymovie

import academy.android.mymovie.model.Actor
import academy.android.mymovie.model.Movie

class MoviesDatabase {
    private val moviesList: ArrayList<Movie> = arrayListOf(
        Movie(
            "Avengers: End game",
            137,
            "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos\' actions and restore balance to the universe.",
            4.0f,
            125,
            "Action, Adventure, Fantasy",
            13,
            true,
            "https://cdn2.lamag.com/wp-content/uploads/sites/6/2019/04/avengers-endgame-disney-1068x601.jpg",
            "https://www.denofgeek.com/wp-content/uploads/2019/04/avengers-endgame-ending-explained.jpg?fit=1280%2C720",
            arrayListOf(
                Actor("Robert Downey Jr.", "https://static.toiimg.com/photo/msid-75805310/75805310.jpg?135092"),
                Actor("Chris Evans", "https://pcdn.columbian.com/wp-content/uploads/2020/08/People_Chris_Evans_23890.jpg-faf5b-1226x0-c-default.jpg"),
                Actor("Mark Ruffalo", "https://img1.looper.com/img/gallery/mark-ruffalo-b/intro-1550586708.jpg"),
                Actor("Chris Hemwworth", "https://www.biography.com/.image/t_share/MTU0ODUwMjQ0NjIwNzI0MDAx/chris-hemsworth-poses-during-a-photo-call-for-thor-ragnarok-on-october-15-2017-in-sydney-australia-photo-by-mark-metcalfe_getty-images-for-disney-square.jpg"),
                Actor("Robert Downey Jr.", "https://static.toiimg.com/photo/msid-75805310/75805310.jpg?135092"),
                Actor("Chris Evans", "https://pcdn.columbian.com/wp-content/uploads/2020/08/People_Chris_Evans_23890.jpg-faf5b-1226x0-c-default.jpg"),
                Actor("Mark Ruffalo", "https://img1.looper.com/img/gallery/mark-ruffalo-b/intro-1550586708.jpg")
            )
        ),

        Movie(
            "Tenet",
            97,
            "Tenet is a 2020 science fiction action spy thriller film written and directed by Christopher Nolan, who produced it with Emma Thomas. A co-production between the United Kingdom and United States, it stars John David Washington, Robert Pattinson, Elizabeth Debicki, Dimple Kapadia, Michael Caine, and Kenneth Branagh.",
            5.0f,
            98,
            "Action, Sci-Fi, Thriller",
            16,
            false,
            "https://m.media-amazon.com/images/M/MV5BYzg0NGM2NjAtNmIxOC00MDJmLTg5ZmYtYzM0MTE4NWE2NzlhXkEyXkFqcGdeQXVyMTA4NjE0NjEy._V1_.jpg",
            "https://isabellereviewsmovies.files.wordpress.com/2020/09/tenet.jpg",
            null
        ),

        Movie(
            "Black Widow",
            102,
            "Black Widow is an upcoming American superhero film based on the Marvel Comics character of the same name. Produced by Marvel Studios and distributed by Walt Disney Studios Motion Pictures, it is intended to be the 24th film in the Marvel Cinematic Universe (MCU).",
            4.0f,
            38,
            "Action, Adventure, Sci-Fi",
            13,
            true,
            "https://terrigen-cdn-dev.marvel.com/content/prod/1x/blackwidow_lob_crd_04.jpg",
            "https://cdn.mos.cms.futurecdn.net/4L75tkWVDgKtGe7kfoEGP6.jpg",
            arrayListOf(
                Actor("Robert Downey Jr.", "https://static.toiimg.com/photo/msid-75805310/75805310.jpg?135092"),
                Actor("Chris Evans", "https://pcdn.columbian.com/wp-content/uploads/2020/08/People_Chris_Evans_23890.jpg-faf5b-1226x0-c-default.jpg"),
                Actor("Mark Ruffalo", "https://img1.looper.com/img/gallery/mark-ruffalo-b/intro-1550586708.jpg"),
                Actor("Chris Hemwworth", "https://www.biography.com/.image/t_share/MTU0ODUwMjQ0NjIwNzI0MDAx/chris-hemsworth-poses-during-a-photo-call-for-thor-ragnarok-on-october-15-2017-in-sydney-australia-photo-by-mark-metcalfe_getty-images-for-disney-square.jpg")
            )
        )
    )

    fun getMoviesList(): ArrayList<Movie> = moviesList
}