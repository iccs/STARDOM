<?php

namespace Iccs\StardomBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\StardomBundle\Entity\FileTypes
 */
class FileTypes
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $fileId
     */
    private $fileId;

    /**
     * @var text $type
     */
    private $type;


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
     * Set fileId
     *
     * @param integer $fileId
     */
    public function setFileId($fileId)
    {
        $this->fileId = $fileId;
    }

    /**
     * Get fileId
     *
     * @return integer 
     */
    public function getFileId()
    {
        return $this->fileId;
    }

    /**
     * Set type
     *
     * @param text $type
     */
    public function setType($type)
    {
        $this->type = $type;
    }

    /**
     * Get type
     *
     * @return text 
     */
    public function getType()
    {
        return $this->type;
    }
}