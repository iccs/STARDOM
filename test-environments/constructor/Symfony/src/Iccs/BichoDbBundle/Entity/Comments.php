<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\Comments
 */
class Comments
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $issueId
     */
    private $issueId;

    /**
     * @var integer $commentId
     */
    private $commentId;

    /**
     * @var text $text
     */
    private $text;

    /**
     * @var integer $submittedBy
     */
    private $submittedBy;

    /**
     * @var datetime $submittedOn
     */
    private $submittedOn;


    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set issueId
     *
     * @param integer $issueId
     */
    public function setIssueId($issueId)
    {
        $this->issueId = $issueId;
    }

    /**
     * Get issueId
     *
     * @return integer 
     */
    public function getIssueId()
    {
        return $this->issueId;
    }

    /**
     * Set commentId
     *
     * @param integer $commentId
     */
    public function setCommentId($commentId)
    {
        $this->commentId = $commentId;
    }

    /**
     * Get commentId
     *
     * @return integer 
     */
    public function getCommentId()
    {
        return $this->commentId;
    }

    /**
     * Set text
     *
     * @param text $text
     */
    public function setText($text)
    {
        $this->text = $text;
    }

    /**
     * Get text
     *
     * @return text 
     */
    public function getText()
    {
        return $this->text;
    }

    /**
     * Set submittedBy
     *
     * @param integer $submittedBy
     */
    public function setSubmittedBy($submittedBy)
    {
        $this->submittedBy = $submittedBy;
    }

    /**
     * Get submittedBy
     *
     * @return integer 
     */
    public function getSubmittedBy()
    {
        return $this->submittedBy;
    }

    /**
     * Set submittedOn
     *
     * @param datetime $submittedOn
     */
    public function setSubmittedOn($submittedOn)
    {
        $this->submittedOn = $submittedOn;
    }

    /**
     * Get submittedOn
     *
     * @return datetime 
     */
    public function getSubmittedOn()
    {
        return $this->submittedOn;
    }
}