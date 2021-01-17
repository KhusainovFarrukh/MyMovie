package academy.android.mymovie.data

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    val runtime: Int,
    val genres: List<Genre>,
    val actors: List<Actor>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readGenreList(),
        parcel.readActorList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(overview)
        parcel.writeString(poster)
        parcel.writeString(backdrop)
        parcel.writeFloat(ratings)
        parcel.writeInt(numberOfRatings)
        parcel.writeInt(minimumAge)
        parcel.writeInt(runtime)
        parcel.writeGenreList(genres)
        parcel.writeActorList(actors)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}

private fun Parcel.readGenreList(): List<Genre> {
    val size = readInt()
    val output = ArrayList<Genre>(size)
    if (size != 0) {
        for (i in 0 until size) {
            output.add(readParcelable(Genre::class.java.classLoader)!!)
        }
    }

    return output
}


fun Parcel.writeGenreList(input: List<Genre>) {
    writeInt(input.size)
    input.forEach {
        writeParcelable(it, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
    }
}

private fun Parcel.readActorList(): List<Actor> {
    val size = readInt()
    val output = ArrayList<Actor>(size)
    if (size != 0) {
        for (i in 0 until size) {
            output.add(readParcelable(Genre::class.java.classLoader)!!)
        }
    }

    return output
}

private fun Parcel.writeActorList(input: List<Actor>?) {
    writeInt(input?.size ?: 0)
    input?.forEach {
        writeParcelable(it, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
    }
}

