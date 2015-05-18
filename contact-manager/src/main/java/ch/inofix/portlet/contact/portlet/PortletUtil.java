package ch.inofix.portlet.contact.portlet;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;

import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.ExpertiseLevel;
import ezvcard.parameter.HobbyLevel;
import ezvcard.parameter.ImageType;
import ezvcard.parameter.ImppType;
import ezvcard.parameter.InterestLevel;
import ezvcard.parameter.SoundType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Anniversary;
import ezvcard.property.Birthday;
import ezvcard.property.Birthplace;
import ezvcard.property.CalendarRequestUri;
import ezvcard.property.CalendarUri;
import ezvcard.property.Deathdate;
import ezvcard.property.Deathplace;
import ezvcard.property.Email;
import ezvcard.property.Expertise;
import ezvcard.property.FreeBusyUrl;
import ezvcard.property.Gender;
import ezvcard.property.Hobby;
import ezvcard.property.Impp;
import ezvcard.property.Interest;
import ezvcard.property.Kind;
import ezvcard.property.Logo;
import ezvcard.property.Member;
import ezvcard.property.Nickname;
import ezvcard.property.Note;
import ezvcard.property.Organization;
import ezvcard.property.Photo;
import ezvcard.property.ProductId;
import ezvcard.property.Profile;
import ezvcard.property.Revision;
import ezvcard.property.Role;
import ezvcard.property.SortString;
import ezvcard.property.Sound;
import ezvcard.property.Source;
import ezvcard.property.SourceDisplayText;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.Timezone;
import ezvcard.property.Title;
import ezvcard.property.Uid;
import ezvcard.property.Url;

/**
 * Utility methods used by the ContactManagerPortlet. Based on the model of the
 * ActionUtil of Liferay proper.
 * 
 * @author Christian Berndt
 * @created 2015-05-16 15:31
 * @modified 2015-05-18 22:19
 * @version 1.0.1
 *
 */
public class PortletUtil {

	// Enable logging for this class
	private static Log log = LogFactoryUtil.getLog(PortletUtil.class.getName());

	@SuppressWarnings("unchecked")
	public static VCard getVCard(HttpServletRequest request, VCard vCard) {

		// Retrieve the parameters and update the received vCard
		// with the parameter values.
		//
		// Since the vCard might be edited with different clients with
		// different capabilities we have to update existing cards with
		// the received parameters.
		//
		// VCard attributes are only set (or unset) if a corresponding request
		// parameter is present, which means, attributes not supported by the
		// contact-manager portlet will be left untouched.

		Map<String, String[]> parameters = request.getParameterMap();

		// Retrieve and set vCard properties (in alphabetical order)

		if (parameters.containsKey("address.country")
				|| parameters.containsKey("address.label")
				|| parameters.containsKey("address.locality")
				|| parameters.containsKey("address.poBox")
				|| parameters.containsKey("address.postalCode")
				|| parameters.containsKey("address.streetAddress")
				|| parameters.containsKey("address.timezone")) {

			vCard.removeProperties(Address.class);

			String[] addressCountries = request
					.getParameterValues("address.country");
			String[] addressLabels = request
					.getParameterValues("address.label");
			String[] addressLanguages = request
					.getParameterValues("address.language");
			String[] addressLocalities = request
					.getParameterValues("address.locality");
			String[] addressPoBoxes = request
					.getParameterValues("address.poBox");
			String[] addressPostalCodes = request
					.getParameterValues("address.postalCode");
			String[] addressRegions = request
					.getParameterValues("address.region");
			String[] addressStreetAddresses = request
					.getParameterValues("address.streetAddress");
			String[] addressTimezones = request
					.getParameterValues("address.timezone");
			String[] addressTypes = request.getParameterValues("address.type");

			for (int i = 0; i < addressTypes.length; i++) {

				// Do not create an address if no relevant parameter is present
				if (Validator.isNotNull(addressCountries[i])
						|| Validator.isNotNull(addressLocalities[i])
						|| Validator.isNotNull(addressPoBoxes[i])
						|| Validator.isNotNull(addressPostalCodes[i])
						|| Validator.isNotNull(addressRegions[i])
						|| Validator.isNotNull(addressStreetAddresses[i])) {

					Address address = new Address();

					if (parameters.containsKey("address.country")) {
						address.setCountry(addressCountries[i]);
					}
					// TODO:
					// address.setGeo(latitude, longitude);
					if (parameters.containsKey("address.label")) {
						address.setLabel(addressLabels[i]);
					}
					if (parameters.containsKey("address.language")) {
						address.setLanguage(addressLanguages[i]);
					}
					if (parameters.containsKey("address.locality")) {
						address.setLocality(addressLocalities[i]);
					}
					if (parameters.containsKey("address.poBox")) {
						address.setPoBox(addressPoBoxes[i]);
					}
					if (parameters.containsKey("address.postalCode")) {
						address.setPostalCode(addressPostalCodes[i]);
					}
					if (parameters.containsKey("address.region")) {
						address.setRegion(addressRegions[i]);
					}
					if (parameters.containsKey("address.streetAddress")) {
						address.setStreetAddress(addressStreetAddresses[i]);
					}
					if (parameters.containsKey("address.timezone")) {
						address.setTimezone(addressTimezones[i]);
					}

					AddressType type = AddressType.find(addressTypes[i]);

					if (type != null) {
						address.addType(type);
					}

					vCard.addAddress(address);
				}
			}
		}

		// TODO: Is the AGENT property still supported?
		// vCard.setAgent(agent);

		if (parameters.containsKey("anniversary.day")
				|| parameters.containsKey("anniversary.month")
				|| parameters.containsKey("anniversary.year")) {

			int anniversaryDay = ParamUtil.getInteger(request,
					"anniversary.day", 1);
			int anniversaryMonth = ParamUtil.getInteger(request,
					"anniversary.month", Calendar.JANUARY);
			int anniversaryYear = ParamUtil.getInteger(request,
					"anniversary.year", 1970);

			Date anniversaryDate = PortalUtil.getDate(anniversaryMonth,
					anniversaryDay, anniversaryYear);
			Anniversary anniversary = new Anniversary(anniversaryDate);
			vCard.setAnniversary(anniversary);
		}

		if (parameters.containsKey("birthday.day")
				|| parameters.containsKey("birthday.month")
				|| parameters.containsKey("birthday.year")) {

			int birthdayDay = ParamUtil.getInteger(request, "birthday.day", 1);
			int birthdayMonth = ParamUtil.getInteger(request, "birthday.month",
					Calendar.JANUARY);
			int birthdayYear = ParamUtil.getInteger(request, "birthday.year",
					1970);
			Date birthDate = PortalUtil.getDate(birthdayMonth, birthdayDay,
					birthdayYear);
			Birthday birthday = new Birthday(birthDate);
			vCard.setBirthday(birthday);
		}

		if (parameters.containsKey("birthplace")) {
			String birthplaceStr = ParamUtil.getString(request, "birthplace");
			Birthplace birthplace = new Birthplace(birthplaceStr);
			vCard.setBirthplace(birthplace);
		}

		if (parameters.containsKey("calendarRequestUri")) {

			vCard.removeProperties(CalendarRequestUri.class);

			String[] calendarRequestUris = ParamUtil.getParameterValues(
					request, "calendarRequestUri");

			for (int i = 0; i < calendarRequestUris.length; i++) {

				if (Validator.isNotNull(calendarRequestUris[i])) {
					CalendarRequestUri calendarRequestUri = new CalendarRequestUri(
							calendarRequestUris[i]);

					vCard.addCalendarRequestUri(calendarRequestUri);
				}
			}
		}

		if (parameters.containsKey("calendarUri")) {

			vCard.removeProperties(CalendarUri.class);

			String[] calendarUris = ParamUtil.getParameterValues(request,
					"calendarUri");

			for (int i = 0; i < calendarUris.length; i++) {

				if (Validator.isNotNull(calendarUris[i])) {
					CalendarUri calendarUri = new CalendarUri(calendarUris[i]);

					vCard.addCalendarUri(calendarUri);
				}
			}
		}

		// TODO: vCard Categories match probably best with Liferay's tags
		// vCard.setCategories(categories);

		// TODO
		// vCard.addClientPidMap(clientPidMap);

		// TODO
		// vCard.setClassification(classification);

		if (parameters.containsKey("deathdate.day")
				|| parameters.containsKey("deathdate.month")
				|| parameters.containsKey("deathdate.year")) {

			int deathdateDay = ParamUtil
					.getInteger(request, "deathdate.day", 1);
			int deathdateMonth = ParamUtil.getInteger(request,
					"deathdate.month", Calendar.JANUARY);
			int deathdateYear = ParamUtil.getInteger(request, "deathdate.year",
					1970);

			Date deathDate = PortalUtil.getDate(deathdateMonth, deathdateDay,
					deathdateYear);
			Deathdate deathdate = new Deathdate(deathDate);
			vCard.setDeathdate(deathdate);
		}

		if (parameters.containsKey("deathplace")) {
			String deathplaceStr = ParamUtil.getString(request, "deathplace");
			Deathplace deathplace = new Deathplace(deathplaceStr);
			vCard.setDeathplace(deathplace);
		}

		if (parameters.containsKey("email.address")) {

			vCard.removeProperties(Email.class);

			String[] emailAddresses = ParamUtil.getParameterValues(request,
					"email.address");
			String[] emailTypes = ParamUtil.getParameterValues(request,
					"email.type");

			for (int i = 0; i < emailAddresses.length; i++) {

				EmailType type = EmailType.find(emailTypes[i]);

				if (Validator.isNotNull(emailAddresses[i])) {

					Email email = new Email(emailAddresses[i]);

					if (type != null) {
						email.addType(type);
					}
					vCard.addEmail(email);
				}
			}
		}

		if (parameters.containsKey("expertise")) {

			vCard.removeProperties(Expertise.class);

			String[] expertises = ParamUtil.getParameterValues(request,
					"expertise");
			String[] expertiseLevels = ParamUtil.getParameterValues(request,
					"expertise.level");

			for (int i = 0; i < expertises.length; i++) {

				if (Validator.isNotNull(expertises[i])) {

					Expertise expertise = new Expertise(expertises[i]);
					ExpertiseLevel level = ExpertiseLevel
							.find(expertiseLevels[i]);
					expertise.setLevel(level);
					vCard.addExpertise(expertise);
				}
			}
		}

		// TODO: add or setFormattedName?
		if (parameters.containsKey("formattedName")) {
			String formattedName = ParamUtil
					.getString(request, "formattedName");
			vCard.setFormattedName(formattedName);
		}

		if (parameters.containsKey("freeBusyUrl")) {

			vCard.removeProperties(FreeBusyUrl.class);

			String[] freeBusyUrls = ParamUtil.getParameterValues(request,
					"freeBusyUrl");

			for (int i = 0; i < freeBusyUrls.length; i++) {

				if (Validator.isNotNull(freeBusyUrls[i])) {
					FreeBusyUrl freeBusyUrl = new FreeBusyUrl(freeBusyUrls[i]);
					vCard.addFbUrl(freeBusyUrl);
				}
			}
		}

		if (parameters.containsKey("gender")) {
			String genderStr = ParamUtil.getString(request, "gender");
			Gender gender = new Gender(genderStr);
			vCard.setGender(gender);
		}

		// TODO: add or set GEO?
		// Geo geo = new Geo(latitude, longitude);
		// vCard.setGeo(geo);

		if (parameters.containsKey("hobby")) {

			vCard.removeProperties(Hobby.class);

			String[] hobbies = ParamUtil.getParameterValues(request, "hobby");
			String[] hobbyLevels = ParamUtil.getParameterValues(request,
					"hobby.level");

			for (int i = 0; i < hobbies.length; i++) {

				if (Validator.isNotNull(hobbies[i])) {

					Hobby hobby = new Hobby(hobbies[i]);
					HobbyLevel level = HobbyLevel.find(hobbyLevels[i]);
					hobby.setLevel(level);

					vCard.addHobby(hobby);
				}
			}
		}

		if (parameters.containsKey("impp.uri")) {

			vCard.removeProperties(Impp.class);

			String[] imppProtocols = ParamUtil.getParameterValues(request,
					"impp.protocol");
			String[] imppTypes = ParamUtil.getParameterValues(request,
					"impp.type");
			String[] imppUris = ParamUtil.getParameterValues(request,
					"impp.uri");

			for (int i = 0; i < imppUris.length; i++) {

				ImppType type = ImppType.find(imppTypes[i]);

				log.info("imppTypes[i] = " + imppTypes[i]);

				if (Validator.isNotNull(imppUris[i])) {

					// TODO: check uri format
					String uri = imppProtocols[i] + ":" + imppUris[i];
					Impp impp = new Impp(uri);
					if (type != null) {
						impp.addType(type);
					}
					vCard.addImpp(impp);
				}
			}
		}

		if (parameters.containsKey("interest")) {

			vCard.removeProperties(Interest.class);

			String[] interests = ParamUtil.getParameterValues(request,
					"interest");
			String[] interestLevels = ParamUtil.getParameterValues(request,
					"interest.level");

			for (int i = 0; i < interests.length; i++) {

				if (Validator.isNotNull(interests[i])) {

					Interest interest = new Interest(interests[i]);
					InterestLevel level = InterestLevel.find(interestLevels[i]);
					interest.setLevel(level);
					vCard.addInterest(interest);

				}
			}
		}

		// TODO: Do we need keys?
		// vCard.addKey(key);

		if (parameters.containsKey("kind")) {
			String kindStr = ParamUtil.getString(request, "kind");
			Kind kind = new Kind(kindStr);
			vCard.setKind(kind);
		}

		// TODO: How to handle the vCard's language?
		// vCard.addLanguage(language);

		if (parameters.containsKey("logo.url")) {

			vCard.removeProperties(Logo.class);

			String[] logoUrls = ParamUtil.getParameterValues(request,
					"logo.url");
			String[] logoTypes = ParamUtil.getParameterValues(request,
					"logo.type");

			for (int i = 0; i < logoUrls.length; i++) {

				if (Validator.isNotNull(logoUrls[i])) {

					// TODO: How to handle mediaType and extension?
					String mediaType = null;
					String extension = null;
					ImageType type = ImageType.find(logoTypes[i], mediaType,
							extension);
					Logo logo = new Logo(logoUrls[i], type);
					vCard.addLogo(logo);
				}
			}
		}

		if (parameters.containsKey("member")) {

			vCard.removeProperties(Member.class);

			String[] members = ParamUtil.getParameterValues(request, "member");

			for (int i = 0; i < members.length; i++) {

				if (Validator.isNotNull(members[i])) {

					Member member = new Member(members[i]);
					vCard.addMember(member);

				}
			}
		}

		if (parameters.containsKey("nickname")) {
			String nicknameStr = ParamUtil.getString(request, "nickname");
			Nickname nickname = new Nickname();
			// TODO: Add support for multiple nicknames
			nickname.addValue(nicknameStr);
			vCard.setNickname(nickname);
		}

		if (parameters.containsKey("note")) {

			vCard.removeProperties(Note.class);

			String[] notes = ParamUtil.getParameterValues(request, "note");

			for (int i = 0; i < notes.length; i++) {

				Note note = new Note(notes[i]);

				if (Validator.isNotNull(notes[i])) {
					vCard.addNote(note);
				}
			}
		}

		if (parameters.containsKey("organization")) {

			vCard.removeProperties(Organization.class);

			String[] organizations = ParamUtil.getParameterValues(request,
					"organization");

			Organization organization = new Organization();

			for (int i = 0; i < organizations.length; i++) {

				if (Validator.isNotNull(organizations[i])) {
					organization.addValue(organizations[i]);
				}
			}

			vCard.addOrganization(organization);

		}

		// TODO
		// vCard.addOrgDirectory(orgDirectory);

		// TODO
		// vCard.addOrphanedLabel(label);

		if (parameters.containsKey("phone.number")) {

			String[] phoneNumbers = ParamUtil.getParameterValues(request,
					"phone.number");
			String[] phoneTypes = ParamUtil.getParameterValues(request,
					"phone.type");

			vCard.removeProperties(Telephone.class);

			for (int i = 0; i < phoneNumbers.length; i++) {

				TelephoneType type = TelephoneType.find(phoneTypes[i]);

				if (Validator.isNotNull(phoneNumbers[i])) {
					Telephone phone = new Telephone(phoneNumbers[i]);
					if (type != null) {
						phone.addType(type);
					}
					vCard.addTelephoneNumber(phone);
				}
			}
		}

		if (parameters.containsKey("photo.url")) {

			vCard.removeProperties(Photo.class);

			String[] photoUrls = ParamUtil.getParameterValues(request,
					"photo.url");
			String[] photoTypes = ParamUtil.getParameterValues(request,
					"photo.type");

			for (int i = 0; i < photoUrls.length; i++) {

				if (Validator.isNotNull(photoUrls[i])) {

					// TODO: How to handle mediaType and extension?
					String mediaType = null;
					String extension = null;
					ImageType type = ImageType.find(photoTypes[i], mediaType,
							extension);
					Photo photo = new Photo(photoUrls[i], type);
					vCard.addPhoto(photo);
				}
			}
		}

		if (parameters.containsKey("productId")) {
			String productIdStr = ParamUtil.getString(request, "productId");
			ProductId productId = new ProductId(productIdStr);
			vCard.setProductId(productId);
		}

		if (parameters.containsKey("profile")) {
			Profile profile = new Profile();
			String profileStr = ParamUtil.getString(request, "profile");
			profile.setValue(profileStr);
			vCard.setProfile(profile);
		}

		// TODO
		// vCard.addRelated(related);

		Revision revision = new Revision(new Date());
		vCard.setRevision(revision);

		if (parameters.containsKey("role")) {

			vCard.removeProperties(Role.class);

			String[] roles = ParamUtil.getParameterValues(request, "role");

			for (int i = 0; i < roles.length; i++) {

				if (Validator.isNotNull(roles[i])) {
					Role role = new Role(roles[i]);

					vCard.addRole(role);
				}
			}
		}

		if (parameters.containsKey("sortString")) {
			String sortStringStr = ParamUtil.getString(request, "sortString");
			SortString sortString = new SortString(sortStringStr);
			vCard.setSortString(sortString);
		}

		if (parameters.containsKey("sound.url")) {

			vCard.removeProperties(Sound.class);

			String[] soundUrls = ParamUtil.getParameterValues(request,
					"sound.url");
			String[] soundTypes = ParamUtil.getParameterValues(request,
					"sound.type");

			for (int i = 0; i < soundUrls.length; i++) {

				if (Validator.isNotNull(soundUrls[i])) {

					// TODO: How to handle mediaType and extension?
					String mediaType = null;
					String extension = null;
					SoundType type = SoundType.find(soundTypes[i], mediaType,
							extension);

					Sound sound = new Sound(soundUrls[i], type);

					vCard.addSound(sound);
				}
			}
		}

		if (parameters.containsKey("source.uri")) {

			vCard.removeProperties(Source.class);

			String[] sourceUris = ParamUtil.getParameterValues(request,
					"source.uri");

			for (int i = 0; i < sourceUris.length; i++) {

				if (Validator.isNotNull(sourceUris[i])) {
					Source sourceUri = new Source(sourceUris[i]);

					vCard.addSource(sourceUri);
				}
			}
		}

		if (parameters.containsKey("sourceDisplayText")) {
			String sourceDisplayTextStr = ParamUtil.getString(request,
					"sourceDisplayText");
			SourceDisplayText sourceDisplayText = new SourceDisplayText(
					sourceDisplayTextStr);
			vCard.setSourceDisplayText(sourceDisplayText);
		}

		if (parameters.containsKey("structuredName.additional")
				|| parameters.containsKey("structuredName.family")
				|| parameters.containsKey("structuredName.given")
				|| parameters.containsKey("structuredName.prefix")
				|| parameters.containsKey("structuredName.suffix")) {

			StructuredName structuredName = new StructuredName();

			String snAdditional = ParamUtil.getString(request,
					"structuredName.additional");
			String snFamily = ParamUtil.getString(request,
					"structuredName.family");
			String snGiven = ParamUtil.getString(request,
					"structuredName.given");
			String snPrefix = ParamUtil.getString(request,
					"structuredName.prefix");
			String snSuffix = ParamUtil.getString(request,
					"structuredName.suffix");

			structuredName.removeParameter("additional");
			structuredName.addAdditional(snAdditional);
			structuredName.setFamily(snFamily);
			structuredName.setGiven(snGiven);
			structuredName.removeParameter("prefix");
			structuredName.addPrefix(snPrefix);
			structuredName.removeParameter("suffix");
			structuredName.addPrefix(snSuffix);
			structuredName.setSortAs(snFamily, snGiven);

			vCard.setStructuredName(structuredName);

		}

		if (parameters.containsKey("timezone")) {
			// TODO check alternative methods for setting the timezone
			String timezoneStr = ParamUtil.getString(request, "timezone");
			Timezone timezone = new Timezone(timezoneStr);
			vCard.setTimezone(timezone);
		}

		if (parameters.containsKey("title")) {

			vCard.removeProperties(Title.class);

			String[] titles = ParamUtil.getParameterValues(request, "title");

			for (int i = 0; i < titles.length; i++) {

				if (Validator.isNotNull(titles[i])) {
					Title title = new Title(titles[i]);

					vCard.addTitle(title);
				}
			}
		}

		if (parameters.containsKey("uid")) {
			String uidStr = ParamUtil.getString(request, "uid");
			if (Validator.isNotNull(uidStr)) {
				Uid uid = new Uid(uidStr);
				vCard.setUid(uid);
			}
		}

		if (parameters.containsKey("url.address")) {

			vCard.removeProperties(Url.class);

			String[] urlAddresses = ParamUtil.getParameterValues(request,
					"url.address");
			String[] urlTypes = ParamUtil.getParameterValues(request,
					"url.type");

			for (int i = 0; i < urlAddresses.length; i++) {

				if (Validator.isNotNull(urlAddresses[i])) {
					Url url = new Url(urlAddresses[i]);
					url.setType(urlTypes[i]);

					vCard.addUrl(url);
				}
			}
		}

		vCard.setVersion(VCardVersion.V4_0);

		// TODO
		// vCard.addXml(xml);

		return vCard;
	}

	/**
	 * 
	 * @param portletRequest
	 * @param vCard
	 * @return
	 * @since 1.0.0
	 */
	public static VCard getVCard(PortletRequest portletRequest, VCard vCard) {

		HttpServletRequest request = PortalUtil
				.getHttpServletRequest(portletRequest);

		return getVCard(request, vCard);
	}

}