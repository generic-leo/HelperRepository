package com.leo.homeloan.data.pojo.remote

data class ApplicantList(val loanDetails: LoanDetailRemote, val aapList: ArrayList<ApplicantDetail>)
data class ApplicantDetail(
                            val empId: EmploymentDetailRemote,
                            val personalId: PersonalDetailRemote,
                            val contactId: ContactDetailRemote,
                            val documentsDetails: ArrayList<DocumentDetailRemote>,
                            val loanId: Int)

data class LoanDetailRemote(
                                val purposeOfLoan: String?,
                                val tenureRequired: String?,
                                val product: String?,
                                val loanamountRequired: String?,
                                val typeofInterest: String?,
                                val promotion: String?)

data class EmploymentDetailRemote(
                                val TWEYears:String?,
                                val DIBMonths: String?,
                                val officeState: String?,
                                val officeState_desc: String?,
                                val officePincode: String?,
                                val officeCity: String?,
                                val officeCity_desc: String?,
                                val companyName: String?,
                                val TWEMonths: String?,
                                val officeCountry: String?,
                                val occupationType: String?,
                                val DIBYears: String?,
                                val officeAddressLine2: String?,
                                val officeOfficialEmail: String?,
                                val officeAddressLine1: String?,
                                val officeLandlineNo: String?,
                                val officeAddressLine3: String?,
                                val officeStdcode: String?,
                                val officeCountry_desc: String?,
                                val companyName_desc: String?)

data class PersonalDetailRemote(
                            val firstName: String?,
                            val lastName: String?,
                            val qualification: String?,
                            val panno: String?,
                            val middleName: String?,
                            val aadharNo: String?,
                            val noOfDependants: String?,
                            val category: String?,
                            val maritalStatus: String?,
                            val religion: String?,
                            val verifyAadharNo: String?,
                            val verifyPanno: String?,
                            var category_desc: String?,
                            var qualification_desc: String?,
                            var religion_desc: String?,
                            var noOfDependants_child: String?,
                            var noOfDependants_other: String?)

data class ContactDetailRemote(
                            val PreferredMailingAddress:String?,
                            val mobileNumber:String?,
                            val emailid: String?,
                            val residenceStatus: String?,
                            val residenceSTD: String?,
                            val residenceLandline: String?)

data class DocumentDetailRemote(
        val path: String?,
        val name: String?
)
