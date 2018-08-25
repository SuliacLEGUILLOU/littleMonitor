package s18alg.myapplication.model

import android.arch.persistence.room.TypeConverter

enum class Profile {
    Personal,
    Professional,
    Other;

    companion object {
        private val ENUMS = Profile.values()

        fun of(value: Int): Profile {
            return ENUMS[value]
        }
    }
}

class ProfileConverter {
    @TypeConverter
    fun toProfile(ordinal: Int) : Profile {
        return Profile.of(ordinal)
    }

    @TypeConverter
    fun toInteger(p: Profile) : Int {
        return p.ordinal
    }
}