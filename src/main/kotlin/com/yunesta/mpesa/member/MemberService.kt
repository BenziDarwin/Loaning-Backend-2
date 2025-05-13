package com.yunesta.mpesa.member

import com.yunesta.mpesa.collector.CollectorRepository
import com.yunesta.mpesa.helpers.MessageNotifications
import com.yunesta.mpesa.town.TownRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val townRepository: TownRepository,
    private val collectorRepository: CollectorRepository,
    private val messageNotifications: MessageNotifications
) {

    // Create a new member
    fun createMember(member: Member): Member {
        val town = townRepository.findById(member.town.id).orElseThrow { IllegalArgumentException("Town not found") }
        val collector = collectorRepository.findById(member.collector!!.id).orElseThrow { IllegalArgumentException("Collector not found") }

        val member = Member(
            firstName = member.firstName,
            secondName = member.secondName,
            thirdName = member.thirdName,
            dateOfBirth = member.dateOfBirth,
            gender = member.gender,
            town = town,
            collector = collector,
            email = member.email,
            nin = member.nin,
            phoneNumber = member.phoneNumber,
            profilePicture = member.profilePicture,
            idFrontImage = member.idFrontImage,
            idBackImage = member.idBackImage,
            status = member.status,
            grossLent = member.grossLent,
            netProfit = member.netProfit,
            totalCycle = member.totalCycle,
            clientValue = member.clientValue
        )
        val message = "Welcome to Yunesta Kenya. We want to see your business Prosper."
        messageNotifications.sendSms(listOf(member.phoneNumber), message)
        return memberRepository.save(member)
    }

    // Get all members
    fun getAllMembers(): List<Member> {
        return memberRepository.findAll()
    }

    // Get member by ID
    fun getMemberById(id: String): Member {
        return memberRepository.findById(id).orElseThrow { IllegalArgumentException("Member not found") }
    }

    // Update a member
    fun updateMember(updatedMember: Member, id: String): Member {
        val member = memberRepository.findById(id).orElseThrow { IllegalArgumentException("Member not found") }
        val town = townRepository.findById(updatedMember.town.id).orElseThrow { IllegalArgumentException("Town not found") }
        val collector = collectorRepository.findById(updatedMember.collector!!.id).orElseThrow { IllegalArgumentException("Collector not found") }

        member.firstName = updatedMember.firstName
        member.secondName = updatedMember.secondName
        member.thirdName = updatedMember.thirdName
        member.dateOfBirth = updatedMember.dateOfBirth
        member.gender = updatedMember.gender
        member.town = town
        member.collector = collector
        member.email = updatedMember.email
        member.nin = updatedMember.nin
        member.phoneNumber = updatedMember.phoneNumber
        member.profilePicture = updatedMember.profilePicture
        member.idFrontImage = updatedMember.idFrontImage
        member.idBackImage = updatedMember.idBackImage
        member.status = updatedMember.status
        member.grossLent = updatedMember.grossLent
        member.netProfit = updatedMember.netProfit
        member.totalCycle = updatedMember.totalCycle
        member.clientValue = updatedMember.clientValue

        return memberRepository.save(member)
    }

    // Delete member by ID
    fun deleteMember(id: String) {
        if (!memberRepository.existsById(id)) {
            throw IllegalArgumentException("Member not found")
        }
        memberRepository.deleteById(id)
    }
}
